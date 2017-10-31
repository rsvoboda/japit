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
import java.util.List;
import java.util.TreeSet;
import org.jboss.japit.core.Archive;

/**
 *
 * @author Rostislav Svoboda
 */
public class Reporting {

    public static void generateReports(List<Archive> archives, boolean isTextOutputDisbled, File txtOutputDir, File htmlOutputDir) {

        TreeSet<Archive> sortedArchives = new TreeSet<>(archives);  // to sort them

        if (!isTextOutputDisbled) {
            new TextReportGenerator().generateReport(sortedArchives);
        }

        if (txtOutputDir != null) {
            new TxtFileReportGenerator(new File(txtOutputDir, "report.txt")).generateReport(sortedArchives);
        }

        if (htmlOutputDir != null) {
            new HtmlFileReportGenerator(htmlOutputDir).generateReport(sortedArchives);
        }
    }

    public static void generateDiffReports(List<Archive> archives, boolean isTextOutputDisbled,
            File txtOutputDir, File htmlOutputDir, boolean ignoreClassVersion, boolean suppressArchiveReport, boolean enableDeclaredItems) {

        if (!isTextOutputDisbled) {
            new TextReportGenerator().generateDiffReport(archives, ignoreClassVersion, suppressArchiveReport, enableDeclaredItems);
        }

        if (txtOutputDir != null) {
            new TxtFileReportGenerator(new File(txtOutputDir, "report.txt")).generateDiffReport(archives, ignoreClassVersion, suppressArchiveReport, enableDeclaredItems);
        }

        if (htmlOutputDir != null) {
            new HtmlFileReportGenerator(htmlOutputDir).generateDiffReport(archives, ignoreClassVersion, suppressArchiveReport, enableDeclaredItems);
        }

    }
}
