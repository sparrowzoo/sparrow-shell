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

package com.sparrow.cg.impl;

import com.sparrow.cg.Unloadable;
import com.sparrow.classloader.DynamicClassLoader;
import com.sparrow.protocol.constant.magic.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DynamicCompiler {
    private static Logger logger = LoggerFactory.getLogger(DynamicCompiler.class);

    private static DynamicCompiler ourInstance = new DynamicCompiler();

    private String encoding;

    public static DynamicCompiler getInstance() {
        return ourInstance;
    }

    private URLClassLoader classLoader;

    private String classpath;

    private DynamicCompiler() {
        this.classLoader = (URLClassLoader) this.getClass().getClassLoader();
        this.encoding = StandardCharsets.UTF_8.name();
        URLClassLoader urlClassLoader = this.classLoader;
        this.buildClassPath(urlClassLoader);
    }

    private void buildClassPath(URLClassLoader classLoader) {
        this.classpath = null;
        StringBuilder classPath = new StringBuilder();
        for (URL url : classLoader.getURLs()) {
            String p = url.getFile();
            classPath.append(p).append(File.pathSeparator);
        }
        this.classpath = classPath.toString();
    }

    public void unload(Unloadable unloadable) {
        Class clazz = unloadable.getClass();
        clazz = null;
        unloadable = null;
    }

    public Class<?> source2Class(String fullClassName, String javaCode) {
        long start = System.currentTimeMillis();
        // 获取系统的java 编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            logger.error("compiler is null put jdk/lib/tools.jar to /jre/lib/tools.jar");
            return null;
        }
        ClassFileManager fileManager = null;
        try {
            // 诊断 收集器
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            fileManager = new ClassFileManager(
                    compiler.getStandardFileManager(diagnostics, Locale.CHINA,
                            StandardCharsets.UTF_8));

            List<JavaFileObject> javaFileObjectList = new ArrayList<JavaFileObject>();
            javaFileObjectList.add(new JavaSourceFileObject(fullClassName, javaCode));

            List<String> options = new ArrayList<String>();
            options.add("-encoding");
            options.add(this.encoding);
            options.add("-classpath");
            options.add(this.classpath);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                    diagnostics, options, null, javaFileObjectList);
            boolean success = task.call();
            if (success) {
                JavaClassFileObject javaClassFileObject = fileManager.getJavaClassObject();
                DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(
                        this.classLoader);
                return dynamicClassLoader.loadClass(fullClassName, javaClassFileObject);
            }
            String error = Symbol.EMPTY;
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                error = error + compilePrint(diagnostic);
                logger.info(error);
            }
            return null;
        } catch (Exception e) {
            logger.error("source2Class error ", e);
            return null;
        } finally {
            try {
                if (fileManager != null) {
                    fileManager.close();
                }
            } catch (IOException ignore) {
            }
            long end = System.currentTimeMillis();
            logger.info("init " + fullClassName + " use:" + (end - start) + "ms");
        }
    }

    public Object sourceToObject(String fullClassName, String javaCode)
            throws IllegalAccessException, InstantiationException {
        Class<?> clazz = source2Class(fullClassName, javaCode);
        if (clazz == null) {
            return null;
        }
        return clazz.newInstance();
    }

    private String compilePrint(Diagnostic<?> diagnostic) {
        StringBuilder res = new StringBuilder();
        res.append("Code:[").append(diagnostic.getCode()).append("]\n");
        res.append("Kind:[").append(diagnostic.getKind()).append("]\n");
        res.append("Position:[").append(diagnostic.getPosition()).append("]\n");
        res.append("Start Position:[").append(diagnostic.getStartPosition()).append("]\n");
        res.append("End Position:[").append(diagnostic.getEndPosition()).append("]\n");
        res.append("Source:[").append(diagnostic.getSource()).append("]\n");
        res.append("Message:[").append(diagnostic.getMessage(null)).append("]\n");
        res.append("LineNumber:[").append(diagnostic.getLineNumber()).append("]\n");
        res.append("ColumnNumber:[").append(diagnostic.getColumnNumber()).append("]\n");
        logger.info(res.toString());
        return res.toString();
    }
}
