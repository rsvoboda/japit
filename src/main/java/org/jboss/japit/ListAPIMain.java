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
import java.util.ArrayList;
import java.util.List;
import org.jboss.japit.analyser.JarAnalyser;
import org.jboss.japit.core.Archive;
import org.jboss.japit.reporting.Reporting;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionHandlerFilter;

/**
 *
 * @author Rostislav Svoboda
 */
public class ListAPIMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ListAPIMain().checkOptions(args);
    }

    public void checkOptions(String[] args) {
        ListAPIMainOptions options = new ListAPIMainOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(80);
        List<File> inputFiles = new ArrayList<File>();

        try {
            parser.parseArgument(args);

            if (options.getArguments().isEmpty()) {
                throw new CmdLineException(parser, "No argument is given");
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
            System.err.println("  Example: java " + this.getClass().getCanonicalName() + parser.printExample(OptionHandlerFilter.ALL) + "  JarFile1 [JarFile2 ...]");

            return;
        }

        List<Archive> jarArchives = new JarAnalyser(options).analyseJars(inputFiles);

        Reporting.generateReports(jarArchives, options.isTextOutputDisbled(), options.getTxtOutputDir(), options.getHtmlOutputDir());

    }
}
