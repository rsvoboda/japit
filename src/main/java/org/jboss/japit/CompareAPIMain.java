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
package org.jboss.japit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jboss.japit.analyser.JarAnalyser;
import org.jboss.japit.core.Archive;
import org.jboss.japit.core.ClassDetails;
import org.jboss.japit.core.JarArchive;
import org.jboss.japit.reporting.Reporting;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 *
 * @author Rostislav Svoboda
 */
public class CompareAPIMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new CompareAPIMain().checkOptions(args);
    }
    private boolean failCalled;

    public void checkOptions(String[] args) throws IOException {
        CompareAPIMainOptions options = new CompareAPIMainOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(80);
        List<File> inputFiles = new ArrayList<File>();

        try {
            parser.parseArgument(args);

            if (options.getArguments().isEmpty()) {
                throw new CmdLineException(parser, "No argument is given");
            }
            if (options.getArguments().size() != 2) {
                throw new CmdLineException(parser, "Two arguments are expected");
            }
            if (options.getHtmlOutputDir() != null && options.getHtmlOutputDir().isFile()) {
                throw new CmdLineException(parser, "HTML output must point to the directory");
            }
            if (options.getTxtOutputDir() != null && options.getTxtOutputDir().isFile()) {
                throw new CmdLineException(parser, "TXT output must point to the directory");
            }

            for (String argument : options.getArguments()) {
                File inputFile = new File(argument);
                if (!inputFile.isFile()) {
                    throw new CmdLineException(parser, "Provided argument (" + argument
                            + ") is not a file ");
                }
                inputFiles.add(inputFile);
            }

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java " + this.getClass().getCanonicalName() + parser.printExample(OptionHandlerFilter.ALL) 
                    + "  FirstJarFile SecondJarFile");

            return;
        }

        TreeSet<Archive> jarArchives = JarAnalyser.analyseJars(inputFiles, options.getSelectedFQCN());

//        Reporting.generateDiffReports(jarArchives, options.isTextOutputDisbled(), options.getTxtOutputDir(), 
//                options.getHtmlOutputDir(), options.isIgnoreClassVersion(), options.isSuppressArchiveReport());

        JarArchive first = (JarArchive) jarArchives.pollFirst();
        JarArchive second = (JarArchive) jarArchives.pollFirst();

        System.out.println("First jar: " + first.getFilePath());
        System.out.println("Second jar: " + second.getFilePath());
        System.out.println();
        System.out.println("Classes count: " + first.getClasses().size() + " vs. " + second.getClasses().size());
        System.out.println();

        TreeMap<String, ClassDetails> firstJarClassesMap = new TreeMap<String, ClassDetails>();
        for (ClassDetails firstJarClass : first.getClasses()) {
            firstJarClassesMap.put(firstJarClass.getClassName(), firstJarClass);
        }
        for (ClassDetails secondJarClass : second.getClasses()) {
            System.out.println(secondJarClass.getClassName() + ":");
            failCalled = false;

            ClassDetails firstJarClass = null;
            try {
                firstJarClass = firstJarClassesMap.remove(secondJarClass.getClassName());
                if (firstJarClass == null) {
                    fail("class doesn't exist in first jar");
                    continue;
                }
            } catch (Exception e) {
                fail("class doesn't exist in first jar");
                continue;
            }

//            can be suppressed by option - ignore class version
//            if (firstJarClass.getClassVersion() != secondJarClass.getClassVersion()) {
//                fail("class version doesn't match: " + firstJarClass.getClassVersion() + " vs. " + secondJarClass.getClassVersion());
//            }

            // remove from report diff
//            if (firstJarClass.getReferencedClasses() != secondJarClass.getReferencedClasses()) {
//                fail("referenced classes doesn't match: " + firstJarClass.getReferencedClasses() + " vs. " + secondJarClass.getReferencedClasses());
//            }

            if (firstJarClass.getMethodsCount() != secondJarClass.getMethodsCount()) {
                fail("methods count doesn't match: " + firstJarClass.getMethodsCount() + " vs. " + secondJarClass.getMethodsCount());
            }

            if (firstJarClass.getDeclaredMethodsCount() != secondJarClass.getDeclaredMethodsCount()) {
                fail("declared methods count doesn't match: " + firstJarClass.getDeclaredMethodsCount() + " vs. " + secondJarClass.getDeclaredMethodsCount());
            }

            if (firstJarClass.getFieldsCount() != secondJarClass.getFieldsCount()) {
                fail("fields count doesn't match: " + firstJarClass.getFieldsCount() + " vs. " + secondJarClass.getFieldsCount());
            }

            if (firstJarClass.getDeclaredFieldsCount() != secondJarClass.getDeclaredFieldsCount()) {
                fail("declared fields count doesn't match: " + firstJarClass.getDeclaredFieldsCount() + " vs. " + secondJarClass.getDeclaredFieldsCount());
            }
            
            
            if (! firstJarClass.getSuperclassName().equals(secondJarClass.getSuperclassName())) {
                fail("super class name doesn't match: " + firstJarClass.getSuperclassName() + " vs. " + secondJarClass.getSuperclassName());
            }
            if (! firstJarClass.getOriginalJavaFile().equals(secondJarClass.getOriginalJavaFile())) {
                fail("original java file doesn't match: " + firstJarClass.getOriginalJavaFile() + " vs. " + secondJarClass.getOriginalJavaFile());
            }
            
            for (String firstJarClassMethod : firstJarClass.getMethods()) {
                if (! secondJarClass.getMethods().contains(firstJarClassMethod)) {
                    fail("second jar doesn't contain method " + firstJarClassMethod);
                }
            }
            for (String secondJarClassMethod : secondJarClass.getMethods()) {
                if (! firstJarClass.getMethods().contains(secondJarClassMethod)) {
                    fail("first jar doesn't contain method " + secondJarClassMethod);
                }
            }
            
            for (String firstJarClassField : firstJarClass.getFields()) {
                if (! secondJarClass.getFields().contains(firstJarClassField)) {
                    fail("second jar doesn't contain field " + firstJarClassField);
                }
            }
            for (String secondJarClassField : secondJarClass.getFields()) {
                if (! firstJarClass.getFields().contains(secondJarClassField)) {
                    fail("first jar doesn't contain field " + secondJarClassField);
                }
            }

            if (!failCalled) {
                ok();
            }
        }
        if (firstJarClassesMap.size() > 0) {
            for (ClassDetails firstJarClass : firstJarClassesMap.values()) {
                System.out.println(firstJarClass.getClassName() + ":");
                fail("class doesn't exist in second jar");
            }
        }
    }

    private void fail(String details) {
        System.out.println("  FAIL  --  " + details);
        failCalled = true;
    }

    private void ok() {
        System.out.println("  OK");
    }
}
