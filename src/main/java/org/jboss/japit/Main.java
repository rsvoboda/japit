/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.NamedOptionDef;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.OptionHandler;

import static java.lang.System.exit;

/**
 * JAPIT jar's main class, which only selects right to to which it gives control based on '-cmd' command line option value.
 * 
 * @author Josef Cacek
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MainOptions options = new MainOptions();
        CmdLineParser parser = new JapitCmdLineParser(options);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);

            final List<String> argList = options.getArguments();
            final String[] cmdArgs = argList == null ? new String[0] : argList.toArray(new String[argList.size()]);
            switch (options.getCommand()) {
                case COMPARE:
                    CompareAPIMain.main(cmdArgs);
                    break;
                case LIST:
                default:
                    ListAPIMain.main(cmdArgs);
                    break;
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java -jar japit.jar " + parser.printExample(OptionHandlerFilter.ALL)
                    + " [command arguments]");
        }
    }

    /**
     * Customized {@link CmdLineParser}, which changes behavior of isOption() method. The original implementation returns true
     * for each argument which starts with "-". Implementation in this class only returns true for recognized options.
     * 
     * @author jcacek
     */
    protected static class JapitCmdLineParser extends CmdLineParser {

        public JapitCmdLineParser(Object bean) {
            super(bean);
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected boolean isOption(String name) {
            List<OptionHandler> options = getOptions();
            for (OptionHandler h : options) {
                NamedOptionDef option = (NamedOptionDef) h.option;
                if (name.equals(option.name())) {
                    return true;
                }
                for (String alias : option.aliases()) {
                    if (name.equals(alias)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static List<File> parseArguments(String[] args, BasicOptions options, boolean checkEvenNumberOfArguments, String commandArguments) {
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(80);
        List<File> inputFiles = new ArrayList<>();

        try {
            parser.parseArgument(args);

            if (options.getArguments().isEmpty()) {
                throw new CmdLineException(parser, "No argument is given");
            }
            if (checkEvenNumberOfArguments && options.getArguments().size() % 2 != 0) {
                throw new CmdLineException(parser, "Even number of arguments is expected");
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
            System.err.println("\nOptions of the command are:");
            parser.printUsage(System.err);
            System.err.println("\nArguments of the command are:");
            System.err.println("  " + commandArguments);

            exit(1);
        }
        return inputFiles;
    }

}
