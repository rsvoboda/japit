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

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Rostislav Svoboda
 */
public class Application {

    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ex) {
            System.err.println("Error loading application.properties: " + ex.getMessage());
        }
    }
    public static final String PRODUCT = prop.getProperty("application.name");
    public static final String VERSION = prop.getProperty("application.version");
    public static final String FULL_VERSION = PRODUCT + " " + VERSION;
    public static final String HOMEPAGE = "https://github.com/rsvoboda/japit";

    private Application() {
    }
}
