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
package org.jboss.japit.core;

import java.util.SortedSet;

/**
 *
 * @author Rostislav Svoboda
 */
public class JarArchive implements Archive, Comparable<JarArchive> {

    private String fileName;
    private String filePath;
    private SortedSet<ClassDetails> classes;

    public JarArchive() {
    }

    public JarArchive(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public JarArchive(String fileName, String filePath, SortedSet<ClassDetails> classes) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.classes = classes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public SortedSet<ClassDetails> getClasses() {
        return classes;
    }

    public void setClasses(SortedSet<ClassDetails> classes) {
        this.classes = classes;
    }

    public int compareTo(JarArchive o) {
        return this.filePath.compareTo(o.getFilePath());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.filePath != null ? this.filePath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JarArchive other = (JarArchive) obj;
        return (this.filePath == null) ? (other.filePath == null) : this.filePath.equals(other.filePath);
    }

    @Override
    public String toString() {

        return "File name: " + fileName + "\n"
                + "File path: " + filePath + "\n"
                + classes + "\n";
    }
}
