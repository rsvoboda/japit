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

import java.io.IOException;
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
    public static void main(String[] args) throws IOException {
        new ListAPIMain().checkOptions(args);
    }

    public void checkOptions(String[] args) throws IOException {
        ListAPIMainOptions options = new ListAPIMainOptions();
        CmdLineParser parser = new CmdLineParser(options);
        parser.setUsageWidth(80);

        try {
            parser.parseArgument(args);

            //TODO remove, for debugging only
            System.out.println("--> " + options);

            if (options.getArguments().isEmpty()) {
                throw new CmdLineException(parser, "No argument is given");
            }
            if (options.getHtmlOutputDir() != null && options.getHtmlOutputDir().isFile()) {
                throw new CmdLineException(parser, "HTML output must point to the directory");
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Example: java " + this.getClass().getCanonicalName() + parser.printExample(OptionHandlerFilter.ALL) + "  FileToAnalyze");

            return;
        }
    }
}
