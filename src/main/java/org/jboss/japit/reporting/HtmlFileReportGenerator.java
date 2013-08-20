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
import java.util.Collection;
import java.util.TreeSet;
import org.jboss.japit.core.Archive;
import org.jboss.japit.core.JarArchive;

/**
 *
 * @author Rostislav Svoboda
 */
class HtmlFileReportGenerator implements ReportGenerator {

    private final File htmlOutputDir;

    public HtmlFileReportGenerator(File htmlOutputDir) {
        this.htmlOutputDir = htmlOutputDir;
        if (!htmlOutputDir.exists()) {
            htmlOutputDir.mkdirs();
        }
    }

    public void generateReport(Collection<Archive> archives) {
        Archive firstArchive = archives.iterator().next();
        if (firstArchive instanceof JarArchive) {
            HtmlFactory.generateArchiveReport(archives, htmlOutputDir);
            HtmlFactory.generateCSS(htmlOutputDir);
            HtmlFactory.generateIndex(archives, htmlOutputDir, false, false);
        } else {
            throw new UnsupportedOperationException("Can't generate HTML report, "
                    + firstArchive.getClass().getCanonicalName()
                    + " is not supported yet.");
        }
        System.out.println("HTML report in " + htmlOutputDir.getPath() + " was generated");

    }

    public void generateDiffReport(Collection<Archive> archives, boolean ignoreClassVersion, boolean suppressArchiveReport, boolean enableDeclaredItems) {
        if (!suppressArchiveReport) {
            TreeSet<Archive> sortedArchives = new TreeSet<Archive>(archives);
            HtmlFactory.generateArchiveReport(sortedArchives, htmlOutputDir);
        }
        HtmlFactory.generateCSS(htmlOutputDir);
        HtmlFactory.generateDiffReportFile(archives, htmlOutputDir, ignoreClassVersion, suppressArchiveReport, enableDeclaredItems);
        HtmlFactory.generateIndex(archives, htmlOutputDir, true, suppressArchiveReport);
        System.out.println("HTML report in " + htmlOutputDir.getPath() + " was generated");

    }
}
