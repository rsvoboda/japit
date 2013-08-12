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
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 *
 * @author Rostislav Svoboda
 */
public class CompareAPIMainOptions {

    @Option(name = "-c", aliases = { "--class" }, usage = "List API only for specified class", metaVar="FQCN")
    private String selectedFQCN;   
    
    @Option(name = "-d", aliases = { "--disable-console-output" }, usage = "Disable text output to the console")
    private boolean textOutputDisbled = false;
    
    @Option(name = "-i", aliases = { "--ignore-class-version" }, usage = "Ignore class version in comparison")
    private boolean ignoreClassVersion = false;
    
    @Option(name = "-s", aliases = { "--suppress-archive-report" }, usage = "Suppress archive reports in output")
    private boolean suppressArchiveReport = false;
    
    @Option(name = "-h", aliases = { "--html-output" }, usage = "Enable HTML output and set output directory", metaVar="DIR")
    private File htmlOutputDir;   
    
    @Option(name = "-t", aliases = { "--txt-output" }, usage = "Enable TXT output and set output directory", metaVar="DIR")
    private File txtOutputDir;   
    
    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>();

    public List<String> getArguments() {
        return arguments;
    }

    public String getSelectedFQCN() {
        return selectedFQCN;
    }

    public boolean isTextOutputDisbled() {
        return textOutputDisbled;
    }

    public File getHtmlOutputDir() {
        return htmlOutputDir;
    }

    public File getTxtOutputDir() {
        return txtOutputDir;
    }

    public boolean isIgnoreClassVersion() {
        return ignoreClassVersion;
    }

    public boolean isSuppressArchiveReport() {
        return suppressArchiveReport;
    }
    
    @Override
    public String toString() {
        return "ListAPIMainOptions{" + "selectedFQCN=" + selectedFQCN + ", "
                + "textOutputDisbled=" + textOutputDisbled + ", "
                + "ignoreClassVersion=" + ignoreClassVersion + ", "
                + "suppressArchiveReport=" + suppressArchiveReport + ", "
                + "htmlOutputDir=" + htmlOutputDir + ", "
                + "txtOutputDir=" + txtOutputDir + ", "
                + "arguments=" + arguments + '}';
    }

    
    
}
