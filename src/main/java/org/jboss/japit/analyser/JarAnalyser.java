/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.japit.analyser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jboss.japit.BasicOptions;
import org.jboss.japit.core.Archive;
import org.jboss.japit.core.ClassDetails;
import org.jboss.japit.core.JarArchive;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 *
 * @author Rostislav Svoboda
 */
public class JarAnalyser {

    private final BasicOptions options;
    private final List<Pattern> skipPatterns = new ArrayList<>();

    public JarAnalyser(BasicOptions options) {
        this.options = options;
        if (options.getSkipRegex() != null) {
            for (String str : options.getSkipRegex()) {
                skipPatterns.add(Pattern.compile(str));
            }
        }
    }

    public List<Archive> analyseJars(List<File> inputFiles) {
        List<Archive> jarArchives = new ArrayList<>(inputFiles.size());

        for (File file : inputFiles) {
            try (JarFile jar = new JarFile(file)) {
                JarArchive jarArchive = new JarArchive(file.getName(), file.getCanonicalPath());
                Enumeration<JarEntry> jarEntries = jar.entries();
                TreeSet<ClassDetails> classes = enumerationAsStream(jarEntries)
                        .map(je -> createOptionalClassDetail(jar, je))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toCollection(TreeSet::new));

                jarArchive.setClasses(classes);
                jarArchives.add(jarArchive);
            } catch (IOException ex) {
                System.err.println(file.getName() + " is not regular jar file, skipping it.");
                ex.printStackTrace(System.err);
            }

        }

        return jarArchives;
    }

    private String getReturnTypeName(final CtMethod method) {
        try {
            return method.getReturnType().getName();
        } catch (NotFoundException ex) {
            // javassist tried to load return type class from classpath but couldn't find it, hence the exception;
            // getMessage() contains the full class name
            return ex.getMessage();
        }
    }

    /**
     * Provides stream view on given {@link Enumeration}.
     */
    private <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
            public T next() {
                return e.nextElement();
            }

            public boolean hasNext() {
                return e.hasMoreElements();
            }
        }, Spliterator.ORDERED), false);
    }

    /**
     * Creates {@link ClassDetails} instance wrapped as {@link Optional} for not-to-be-skipped class entries in the jar. For
     * non-class entries and skippable classes returns an empty {@link Optional}.
     */
    private Optional<ClassDetails> createOptionalClassDetail(JarFile jar, JarEntry jarEntry) {
        String entryName = jarEntry.getName();
        if (!entryName.endsWith(".class")) {
            return Optional.empty();
        }

        ClassDetails classDetails = null;
        try (InputStream entryStream = jar.getInputStream(jarEntry)) {
            ClassPool classPool = new ClassPool(true);
            CtClass ctClz = classPool.makeClass(entryStream);
            String className = ctClz.getName();

            if (skipClassName(className)) {
                return Optional.empty();
            }
            classDetails = new ClassDetails(Modifier.toString(ctClz.getModifiers()) + " " + className);
            classDetails.setClassVersion(ctClz.getClassFile().getMajorVersion());
            classDetails.setSuperclassName(ctClz.getClassFile().getSuperclass());
            classDetails.setReferencedClasses(ctClz.getRefClasses().size());
            classDetails.setOriginalJavaFile(ctClz.getClassFile().getSourceFile());
            classDetails.setDeclaredMethodsCount(ctClz.getDeclaredMethods().length);
            classDetails.setDeclaredFieldsCount(ctClz.getDeclaredFields().length);

            TreeSet<String> methods = new TreeSet<>();
            for (CtMethod method : ctClz.getMethods()) {
                methods.add(Modifier.toString(method.getModifiers()) + " " + getReturnTypeName(method) + " "
                        + method.getLongName());
            }
            classDetails.setMethods(methods);

            TreeSet<String> fields = new TreeSet<>();
            for (CtField field : ctClz.getFields()) {
                fields.add(Modifier.toString(field.getModifiers()) + " " + field.getFieldInfo().toString());
            }
            classDetails.setFields(fields);
        } catch (Throwable ie) {
            System.err.println("Exception thrown when processing " + entryName);
            ie.printStackTrace(System.err);
        }
        return Optional.ofNullable(classDetails);
    }

    /**
     * Returns true if the given classname should be skipped based on this analyzer options.
     */
    private boolean skipClassName(String className) {
        final String selectedFQCN = options.getSelectedFQCN();
        if (selectedFQCN != null && !selectedFQCN.equals(className)) {
            return true;
        }
        for (Pattern skipPattern : skipPatterns) {
            if (skipPattern.matcher(className).matches()) {
                return true;
            }
        }
        return false;
    }
}
