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

import java.util.Collection;
import java.util.TreeSet;
import org.jboss.japit.core.Archive;

/**
 *
 * @author Rostislav Svoboda
 */
public class TextReportGenerator implements ReportGenerator {

    public void generateReport(Collection<Archive> archives) {
        for (Archive archive : archives) {
            System.out.println(archive);
        }
    }

    public void generateDiffReport(Collection<Archive> archives, boolean ignoreClassVersion, boolean suppressArchiveReport, boolean enableDeclaredItems) {
        if (!suppressArchiveReport) {
            TreeSet<Archive> sortedArchives = new TreeSet<>(archives);
            generateReport(sortedArchives);
        }
        TextFactory.generateTextDiff(System.out, archives, ignoreClassVersion, enableDeclaredItems);
    }
}
