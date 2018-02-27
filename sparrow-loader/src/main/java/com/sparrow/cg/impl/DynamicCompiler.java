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
import com.sparrow.constant.CONFIG;
import com.sparrow.constant.CONSTANT;
import com.sparrow.constant.magic.SYMBOL;
import com.sparrow.utility.Config;
import com.sparrow.utility.StringUtility;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author harry
 */
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
        this.encoding = Config.getValue(CONFIG.COMPILER_OPTION_ENCODING);
        if (StringUtility.isNullOrEmpty(this.encoding)) {
            this.encoding = CONSTANT.CHARSET_UTF_8;
        }
        this.buildClassPath();
    }

    private void buildClassPath() {
        this.classpath = null;
        StringBuilder sb = new StringBuilder();
        for (URL url : this.classLoader.getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        this.classpath = sb.toString();
    }

    public void unload(Unloadable unloadable) {
        Class clazz = unloadable.getClass();
        clazz = null;
        unloadable = null;
    }

    public Object sourceToObject(String fullClassName, String javaCode)
        throws IllegalAccessException, InstantiationException {
        long start = System.currentTimeMillis();
        Object instance = null;
        // 获取系统的java 编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            logger.error("compiler is null put jdk/lib/tools.jar to /jre/lib/tools.jar");
            return null;
        }

        // 诊断 收集器
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        ClassFileManager fileManager = new ClassFileManager(
            compiler.getStandardFileManager(diagnostics, Locale.CHINA,
                Charset.forName(CONSTANT.CHARSET_UTF_8)));

        List<JavaFileObject> javaFileObjectList = new ArrayList<JavaFileObject>();
        javaFileObjectList.add(new JavaSourceFileObject(fullClassName, javaCode));

        /**
         *
         * 2.用法：javac <选项> <源文件>
         *
         * 3.其中，可能的选项包括：
         *
         * 4.-g 生成所有调试信息
         *
         * 5.-g:none 不生成任何调试信息
         *
         * 6.-g:{lines,vars,source} 只生成某些调试信息
         *
         * 7.-nowarn 不生成任何警告
         *
         * 8.-verbose 输出有关编译器正在执行的操作的消息
         *
         * 9.-deprecation 输出使用已过时的 API 的源位置
         *
         * 10.-classpath <路径> 指定查找用户类文件的位置
         *
         * 11.-cp <路径> 指定查找用户类文件的位置
         *
         * 12.-sourcepath <路径> 指定查找输入源文件的位置
         *
         * 13.-bootclasspath <路径> 覆盖引导类文件的位置
         *
         * 14.-extdirs <目录> 覆盖安装的扩展目录的位置
         *
         * 15.-endorseddirs <目录> 覆盖签名的标准路径的位置
         *
         * 16.-d <目录> 指定存放生成的类文件的位置
         *
         * 17.-encoding <编码> 指定源文件使用的字符编码
         *
         * 18.-source <版本> 提供与指定版本的源兼容性
         *
         * 19.-target <版本> 生成特定 VM 版本的类文件
         *
         * 20.-version 版本信息
         *
         * 21.-help 输出标准选项的提要
         *
         * 22.-X 输出非标准选项的提要
         *
         * 23.-J<标志> 直接将 <标志> 传递给运行时系统
         */
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
            Class<?> clazz = dynamicClassLoader.loadClass(fullClassName, javaClassFileObject);
            instance = clazz.newInstance();
        } else {
            String error = SYMBOL.EMPTY;
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                error = error + compilePrint(diagnostic);
                logger.info(error);
            }
        }
        try {
            fileManager.close();
        } catch (IOException ignore) {
        }
        long end = System.currentTimeMillis();
        logger.info("init " + fullClassName + " use:" + (end - start) + "ms");
        return instance;
    }

    private String compilePrint(Diagnostic<?> diagnostic) {
        StringBuilder res = new StringBuilder();
        res.append("Code:[" + diagnostic.getCode() + "]\n");
        res.append("Kind:[" + diagnostic.getKind() + "]\n");
        res.append("Position:[" + diagnostic.getPosition() + "]\n");
        res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
        res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
        res.append("Source:[" + diagnostic.getSource() + "]\n");
        res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
        res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
        res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
        logger.info(res.toString());
        return res.toString();
    }
}
