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

import java.util.TreeSet;

/**
 *
 * @author Rostislav Svoboda
 */
public class ClassArchive implements Archive, Comparable<ClassArchive> {

    private String className;
    private int classVersion;
    private String superclassName;
    private int referencedClasses;
    private String originalJavaFile;
    private int methodsCount;
    private int declaredMethodsCount;
    private int fieldsCount;
    private int declaredFieldsCount;
    private TreeSet<String> methods;
    private TreeSet<String> fields;

    public ClassArchive(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassVersion() {
        return classVersion;
    }

    public void setClassVersion(int classVersion) {
        this.classVersion = classVersion;
    }

    public String getSuperclassName() {
        return superclassName;
    }

    public void setSuperclassName(String superclassName) {
        this.superclassName = superclassName;
    }

    public int getReferencedClasses() {
        return referencedClasses;
    }

    public void setReferencedClasses(int referencedClasses) {
        this.referencedClasses = referencedClasses;
    }

    public String getOriginalJavaFile() {
        return originalJavaFile;
    }

    public void setOriginalJavaFile(String originalJavaFile) {
        this.originalJavaFile = originalJavaFile;
    }

    public int getMethodsCount() {
        return methods.size();
    }

    public int getDeclaredMethodsCount() {
        return declaredMethodsCount;
    }

    public void setDeclaredMethodsCount(int declaredMethodsCount) {
        this.declaredMethodsCount = declaredMethodsCount;
    }

    public int getFiledsCount() {
        return fields.size();
    }

    public int getDeclaredFieldsCount() {
        return declaredFieldsCount;
    }

    public void setDeclaredFieldsCount(int declaredFieldsCount) {
        this.declaredFieldsCount = declaredFieldsCount;
    }

    public TreeSet<String> getMethods() {
        return methods;
    }

    public void setMethods(TreeSet<String> methods) {
        this.methods = methods;
    }

    public TreeSet<String> getFields() {
        return fields;
    }

    public void setFields(TreeSet<String> fields) {
        this.fields = fields;
    }

    public int compareTo(ClassArchive o) {
        return this.className.compareTo(o.getClassName());
    }

    @Override
    public String toString() {
        String mString = "";
        for (String method : methods) {
            mString = mString + method + "\n";
        }
        String fString = "";
        for (String field : fields) {
            fString = fString + field + "\n";
        }
        return "\nClass file: " + className + "\n"
                + "Class version = " + classVersion + "\n"
                + "Superclass file: " + superclassName + "\n"
                + "Referenced classes count: " + referencedClasses + "\n"
                + "Original Java file: " + originalJavaFile + "\n"
                + "Methods count: " + methodsCount + "\n"
                + "Declared Methods count: " + declaredMethodsCount + "\n"
                + "Fields count: " + fieldsCount + "\n"
                + "Declared Fields count: " + declaredFieldsCount + "\n"
                + "Methods:\n" + mString
                + "Fields:\n" + fString
                + "=================================";
    }
}
