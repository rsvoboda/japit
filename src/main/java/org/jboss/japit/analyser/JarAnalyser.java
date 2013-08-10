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
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.jboss.japit.core.Archive;
import org.jboss.japit.core.ClassDetails;
import org.jboss.japit.core.JarArchive;

/**
 *
 * @author Rostislav Svoboda
 */
public class JarAnalyser {

    public static TreeSet<Archive> analyseJars(List<File> inputFiles, String selectedFQCN) {
        TreeSet<Archive> jarArchives = new TreeSet<Archive>();

        for (File file : inputFiles) {

            JarFile jar = null;
            JarArchive jarArchive = null;
            try {
                jar = new JarFile(file);
                jarArchive = new JarArchive(file.getName(), file.getCanonicalPath());
            } catch (IOException ex) {
                System.err.println(file.getName() + " is not regular jar file, skipping it.");
                ex.printStackTrace();
                continue;
            }
            TreeSet<ClassDetails> classes = new TreeSet<ClassDetails>();

            Enumeration<JarEntry> jarEntries = jar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String entryName = jarEntry.getName();
                InputStream entryStream = null;
                if (entryName.endsWith(".class")) {
                    try {
                        entryStream = jar.getInputStream(jarEntry);

                        ClassPool classPool = new ClassPool();
                        CtClass ctClz = classPool.makeClass(entryStream);

                        if (selectedFQCN == null || (selectedFQCN != null && ctClz.getName().equals(selectedFQCN))) {

                            ClassDetails classArchive = new ClassDetails(ctClz.getName());
                            classArchive.setClassVersion(ctClz.getClassFile().getMajorVersion());
                            classArchive.setSuperclassName(ctClz.getClassFile().getSuperclass());
                            classArchive.setReferencedClasses(ctClz.getRefClasses().size());
                            classArchive.setOriginalJavaFile(ctClz.getClassFile().getSourceFile());
                            classArchive.setDeclaredMethodsCount(ctClz.getDeclaredMethods().length);
                            classArchive.setDeclaredFieldsCount(ctClz.getDeclaredFields().length);

                            TreeSet<String> methods = new TreeSet<String>();
                            for (CtMethod method : ctClz.getMethods()) {
                                methods.add(method.getLongName());
                            }
                            classArchive.setMethods(methods);

                            TreeSet<String> fields = new TreeSet<String>();
                            for (CtField field : ctClz.getFields()) {
                                fields.add(field.getFieldInfo().toString());
                            }
                            classArchive.setFields(fields);

                            classes.add(classArchive);
                        }
                    } catch (Exception ie) {
                        System.err.println("Exception thrown when processing " + entryName);
                        ie.printStackTrace(System.err);
                    } finally {
                        if (entryStream != null) {
                            try {
                                entryStream.close();
                            } catch (IOException ex) {
                                System.err.println("EntryStream couldn't be closed for " + entryName);
                            }
                        }
                    }
                }
            }

            jarArchive.setClasses(classes);

            jarArchives.add(jarArchive);
        }

        return jarArchives;
    }
}