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
package org.jboss.japit.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.TreeSet;
import org.jboss.japit.core.Archive;

/**
 *
 * @author Rostislav Svoboda
 */
public class TxtFileReportGenerator implements ReportGenerator {

    private File outputFile;

    public TxtFileReportGenerator(File outputFile) {
        this.outputFile = outputFile;
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
    }

    public void generateReport(Collection<Archive> archives) {
        try {
            PrintWriter out = new PrintWriter(outputFile);
            for (Archive archive : archives) {
                out.println(archive);
            }
            out.close();
            System.out.println(outputFile.getPath() + " was generated");
        } catch (FileNotFoundException ex) {
            System.err.println("Exception when creating report");
            ex.printStackTrace(System.err);
        }
    }

    public void generateDiffReport(Collection<Archive> archives, boolean ignoreClassVersion, boolean suppressArchiveReport) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            PrintStream out = new PrintStream(fos, true);

            if (!suppressArchiveReport) {
                TreeSet<Archive> sortedArchives = new TreeSet<Archive>(archives);
                for (Archive archive : sortedArchives) {
                    out.println(archive);
                }
            }
            TextFactory.generateTextDiff(out, archives, ignoreClassVersion);

            out.close();
            fos.close();
            System.out.println(outputFile.getPath() + " was generated");
        } catch (IOException ex) {
            System.err.println("Exception when creating report");
            ex.printStackTrace(System.err);
        }
    }
}
