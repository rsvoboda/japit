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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.jboss.japit.core.Archive;

/**
 *
 * @author Rostislav Svoboda
 */
public class Reporting {

    public static void generateReports(TreeSet<Archive> archives, boolean isTextOutputDisbled, File txtOutputDir, File htmlOutputDir) {

        List<ReportGenerator> generators = new ArrayList<ReportGenerator>();
        
        if (!isTextOutputDisbled)  {
            new TextReportGenerator().generateReport(archives);
        }

        if (txtOutputDir != null) {
            new TxtFileReportGenerator(new File(txtOutputDir, "report.txt")).generateReport(archives);
        }

        if (htmlOutputDir != null) {
            new HtmlFileReportGenerator(htmlOutputDir).generateReport(archives);
        }
        
        for (ReportGenerator generator : generators) {
            generator.generateReport(archives);
        }

    }
}
