/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.mq;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtility {
    private static Logger logger = LoggerFactory.getLogger(ClassUtility.class);

    public static List<Class> getClasses(
        String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(Symbol.DOT, Symbol.SLASH);
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<Class> classes = new ArrayList<Class>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equalsIgnoreCase(resource.getProtocol())) {
                File directory = new File(URLDecoder.decode(resource.getFile(), Constant.CHARSET_UTF_8));
                classes.addAll(findClass(directory, packageName));
            } else if ("jar".equalsIgnoreCase(resource.getProtocol())) {
                classes.addAll(findClass(((JarURLConnection) resource.openConnection())
                    .getJarFile(), path));
            }
        }
        return classes;
    }

    private static List<Class> findClass(JarFile jarFile, String packagePath) {
        try {
            List<Class> classes = new ArrayList<Class>();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().startsWith(packagePath) && jarEntry.getName().endsWith(".class")) {
                    Class implClass = Class.forName(jarEntry.getName().replace(Symbol.SLASH, Symbol.DOT)
                        .substring(0, jarEntry.getName().indexOf(Symbol.DOT)));
                    if (!implClass.isInterface()) {
                        classes.add(implClass);
                    }
                }
            }
            return classes;
        } catch (Exception e) {
            logger.error("find class exception packagePath {}", packagePath);
            return null;
        }
    }

    private static List<Class> findClass(File directory, String packageName) {
        List<Class> classes = new ArrayList<Class>();
        if (directory == null || !directory.exists()) {
            return null;
        }
        File[] fileList = directory.listFiles();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                classes.addAll(findClass(file, packageName + Symbol.DOT + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String clazzName = packageName + Symbol.DOT + file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(clazzName));
                } catch (Throwable e) {
                    logger.error("class can't  init {}", clazzName);
                }
            }
        }

        return classes;
    }
}
