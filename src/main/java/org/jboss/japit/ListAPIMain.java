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
import java.util.List;
import org.jboss.japit.analyser.JarAnalyser;
import org.jboss.japit.core.Archive;
import org.jboss.japit.reporting.Reporting;

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
        String commandArguments = "JarFile1 [JarFile2 ...]";
        List<File> inputFiles = Main.parseArguments(args, options, false, commandArguments);

        List<Archive> jarArchives = new JarAnalyser(options).analyseJars(inputFiles);

        Reporting.generateReports(jarArchives, options.isTextOutputDisbled(), options.getTxtOutputDir(), options.getHtmlOutputDir());

    }
}
