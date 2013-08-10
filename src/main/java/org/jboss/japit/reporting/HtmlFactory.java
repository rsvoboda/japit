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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeSet;
import org.jboss.japit.Application;
import org.jboss.japit.core.Archive;
import org.jboss.japit.core.ClassDetails;
import org.jboss.japit.core.JarArchive;

/**
 *
 * @author Rostislav Svoboda
 */
public class HtmlFactory {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private HtmlFactory() {
    }

    public static void generateIndex(TreeSet<Archive> archives, File outputDir) {
        try {
            FileWriter fw = new FileWriter(new File(outputDir, "index.html"));
            BufferedWriter bw = new BufferedWriter(fw, 8192);

            generateHeader(bw, "Index");

            bw.write("<h1>" + Application.FULL_VERSION + "</h1>" + NEW_LINE);
            bw.write("<h2>Archives: </h2>" + NEW_LINE);
            bw.write("<ul>" + NEW_LINE);
            for (Archive archive : archives) {
                bw.write("<li><a href=\"" + convertPathForFileName(archive.getFilePath()) + ".html\">"
                        + archive.getFileName() + "</a> - " + archive.getFilePath());
                bw.write(" (" + getArchiveReportSize(outputDir, archive) + ")</li>" + NEW_LINE);
            }
            bw.write("</ul>" + NEW_LINE);

            if (archives.size() < 2) {
                generateArchiveReportBody(bw, (JarArchive) archives.first());  // check done in HtmlFileReportGenerator
            }

            generateFooter(bw);

            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.err.println("GenerateIndex: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static void generateArchiveReport(TreeSet<Archive> archives, File outputDir) {
        for (Archive archive : archives) {
            try {
                FileWriter fw = new FileWriter(new File(outputDir, convertPathForFileName(archive.getFilePath()) + ".html"));
                BufferedWriter bw = new BufferedWriter(fw, 8192);

                generateHeader(bw, archive.getFileName());

                bw.write("<h1>" + archive.getFileName() + " - " + archive.getFilePath() + "</h1>" + NEW_LINE);
                bw.write("<a href=\"index.html\">Main</a>" + NEW_LINE);
                bw.write("<br/><br/>" + NEW_LINE);

                generateArchiveReportBody(bw, (JarArchive) archive);   // type check done in HtmlFileReportGenerator
                generateFooter(bw);

                bw.flush();
                bw.close();
            } catch (Exception e) {
                System.err.println("generateArchiveReport: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    private static String convertPathForFileName(String path) {
        return path.replaceFirst("/", "").replace("/", "-").replace(":\\", "-").replace("\\", "-").replace(" ", "-");
    }

    private static String getArchiveReportSize(File outputDir, Archive archive) {
        File file = new File(outputDir, convertPathForFileName(archive.getFilePath()) + ".html");
        return ((file.length() / 1024) + 1) + "KB";
    }

    private static void generateHeader(BufferedWriter bw, String title) throws IOException {
        bw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
                + "\"http://www.w3.org/TR/html4/loose.dtd\">" + NEW_LINE);
        bw.write("<html>" + NEW_LINE);
        bw.write("<head>" + NEW_LINE);
        bw.write("  <title>" + Application.FULL_VERSION + ": " + title + "</title>" + NEW_LINE);
        bw.write("  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" + NEW_LINE);
        bw.write("  <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">" + NEW_LINE);
        bw.write("</head>" + NEW_LINE);
        bw.write("<body>" + NEW_LINE);
    }

    private static void generateFooter(BufferedWriter bw) throws IOException {
        bw.write("<hr>" + NEW_LINE);
        bw.write("Generated by: <a href=\"" + Application.HOMEPAGE + "\">"
                + Application.FULL_VERSION + "</a>" + NEW_LINE);
        bw.write("</body>" + NEW_LINE);
        bw.write("</html>" + NEW_LINE);
    }

    private static void generateArchiveReportBody(BufferedWriter bw, JarArchive jarArchive) throws IOException {
        bw.write("<h2>" + jarArchive.getClasses().size() + " classes</h2>" + NEW_LINE);
        String delim = "";
        for (ClassDetails classDetails : jarArchive.getClasses()) {
            bw.write(delim + " <a href=\"#" + classDetails.getClassName() + "\">"
                    + classDetails.getClassName() + "</a> ");
            delim = "||";
        }

        bw.write("<br/><br/>" + NEW_LINE);

        //TODO split into methods for each archive type
        for (ClassDetails classDetails : jarArchive.getClasses()) {
            bw.write("<a name=\"" + classDetails.getClassName() + "\"></a> " + NEW_LINE);
            bw.write("<table>" + NEW_LINE);
            bw.write("  <tr>" + NEW_LINE);
            bw.write("     <th>Class " + classDetails.getClassName() + "</th>" + NEW_LINE);
            bw.write("     <th>&nbsp;</th>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);

            bw.write("  <tr class=\"rowodd\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Class version</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getClassVersion() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);
            bw.write("  <tr class=\"rowodd\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Original Java file</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getOriginalJavaFile() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);
            bw.write("  <tr class=\"rowodd\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Superclass file</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getSuperclassName() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);
            bw.write("  <tr class=\"rowodd\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Referenced classes count</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getReferencedClasses() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);

            bw.write("  <tr class=\"roweven\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Methods / Declared Methods count</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getMethodsCount() + " / " + classDetails.getDeclaredMethodsCount() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);
            bw.write("  <tr class=\"roweven\">" + NEW_LINE);
            bw.write("     <td class=\"heading\">Fields / Declared Fields count</td>" + NEW_LINE);
            bw.write("     <td>" + classDetails.getFieldsCount() + " / " + classDetails.getDeclaredFieldsCount() + "</td>" + NEW_LINE);
            bw.write("  </tr>" + NEW_LINE);

            if (classDetails.getMethodsCount() > 0) {
                bw.write("  <tr class=\"rowodd\">" + NEW_LINE);
                bw.write("     <td class=\"heading\">Methods</td>" + NEW_LINE);
                String mString = "";
                for (String method : classDetails.getMethods()) {
                    mString = mString + method + "<br/>";
                }
                bw.write("     <td>"
                        + mString
                        + "</td>" + NEW_LINE);
                bw.write("  </tr>" + NEW_LINE);
            }
            if (classDetails.getFieldsCount() > 0) {
                bw.write("  <tr class=\"roweven\">" + NEW_LINE);
                bw.write("     <td class=\"heading\">Fields</td>" + NEW_LINE);
                String fString = "";
                for (String field : classDetails.getFields()) {
                    fString = fString + field + "\n";
                }
                bw.write("     <td>"
                        + fString
                        + "</td>" + NEW_LINE);
                bw.write("  </tr>" + NEW_LINE);
            }
            bw.write("</table>" + NEW_LINE);
            bw.write("<br/>" + NEW_LINE);
        }
    }

    public static void generateCSS(File outputDir) {
        byte buffer[] = new byte[8192];
        int bytesRead;

        InputStream is = null;
        OutputStream os = null;
        try {
            is = HtmlFactory.class.getClassLoader().getResourceAsStream("style.css");
            os = new FileOutputStream(new File(outputDir, "style.css"));

            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
        } catch (Exception e) {
            System.err.println("GenerateCSS: " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            } // Ignore

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {
            } // Ignore
        }
    }
}
