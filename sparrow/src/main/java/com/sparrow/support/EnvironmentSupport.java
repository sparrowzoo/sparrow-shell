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

package com.sparrow.support;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.ConfigUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class EnvironmentSupport {
    private static Logger logger = LoggerFactory.getLogger(EnvironmentSupport.class);

    private static EnvironmentSupport environmentSupport = new EnvironmentSupport();

    public static EnvironmentSupport getInstance() {
        return environmentSupport;
    }

    private EnvironmentSupport() {

    }

    /**
     * 获取当前classes文件夹../WebRoot/WEB-INF/classes/
     *
     * @return
     */
    public String getClassesPhysicPath() {
        URL url = Thread.currentThread().getContextClassLoader()
            .getResource("");
        if (url != null) {
            return url.getPath().replace("%20", " ");
        }
        String path = this.getClass().getProtectionDomain().getCodeSource()
            .getLocation().getPath();
        return path.substring(0, path.lastIndexOf(Symbol.SLASH));
    }

    /**
     * 获取当WebRoot文件夹
     *
     * @return
     */
    public String getWebRootPhysicPath() {
        String path = getClassesPhysicPath();
        return path.substring(0, path.indexOf("WEB-INF"));
    }

    public String getApplicationSourcePath() {
        String path = getClassesPhysicPath();
        if (path.contains("/bin")) {
            path = path.replace("/bin", "/src");
        } else if (path.contains("/target")) {
            path = path.substring(0, path.indexOf("/target"));
        }
        return path;
    }

    public String getRootPath() {
        String path = getClassesPhysicPath();
        if (path.contains("/bin")) {
            return path.replace("/bin", "/src");
        }
        return path.substring(0, path.indexOf("WEB-INF"));
    }

    /**
     * 获取当WEB-INF文件夹
     *
     * @return
     */
    public String getWebInfPhysicPath() {
        String path = getClassesPhysicPath();
        return path.substring(0, path.indexOf("classes"));
    }

    public String getLibPhysicPath() {
        String path = getClassesPhysicPath();
        return path.substring(0, path.indexOf("classes")) + "/lib";
    }

    public String getWorkspace() {
        if (ConfigUtility.getValue(Constant.WORKSPACE) != null) {
            return ConfigUtility.getValue(Constant.WORKSPACE);
        }
        String classPath = this.getClassesPhysicPath();
        if (classPath.contains("/bin")) {
            classPath = classPath.substring(0, classPath.indexOf("/bin"));
        } else if (classPath.contains("/target")) {
            classPath = classPath.substring(0, classPath.indexOf("/target"));
        } else if (classPath.contains("/WebRoot")) {
            classPath = classPath.substring(0, classPath.indexOf("/WebRoot"));
        }
        classPath = classPath.substring(0, classPath.lastIndexOf(Symbol.SLASH));
        if (classPath.startsWith(Symbol.SLASH)) {
            classPath = classPath.substring(1);
        }
        if (classPath.contains(Symbol.SLASH)) {
            classPath = classPath.replace(Symbol.SLASH, Symbol.BACKSLASH);
        }
        return classPath;
    }

    /**
     * 文件相对路径 path 不以’/'开头时默认是从此类所在的包下取资源， 以’/'开头则是从ClassPath根下获取。其只是通过path构造一个绝对路径，最终还是由ClassLoader获取资源。
     * <p/>
     * <p/>
     * tomcat exception :org.apache.catalina.loader.WebappClassLoader findResourceInternal INFO: Illegal access: this
     * web application instance has been stopped already.  Could not load com/sparrow/support/..  The eventual following
     * stack trace is caused by an error thrown for debugging purposes as well as to attempt to terminate the thread
     * which caused the illegal access, and has no functional impact. java.lang.NullPointerException at
     * org.apache.catalina.loader.WebappClassLoader.getResource(WebappClassLoader.java:1604) at
     * java.lang.Class.getResource(Class.java:2076) at com.sparrow.support.EnvironmentSupport.getFileInputStream(EnvironmentSupport.java:123)
     * <p/>
     * resolve solution :reloadable="false"
     * <p/>
     * http://user.qzone.qq.com/492006183/blog/1453881337
     *
     * @param relativeFileName
     * @return
     */
    public InputStream getFileInputStream(String relativeFileName) throws FileNotFoundException {
        InputStream fileInputStream = null;
        URL url = EnvironmentSupport.class.getResource(relativeFileName);
        if (url != null) {
            //xxx.getClass().getResourceAsStream("xx.properties") 有缓存
            try {
                fileInputStream = url.openStream();
            } catch (IOException e) {
                logger.error("input stream error", e);
                throw new FileNotFoundException(e.getMessage());
            }
        }
        if (fileInputStream == null) {
            fileInputStream = new FileInputStream(new File(relativeFileName));
        }
        return fileInputStream;
    }

    /**
     * 读文件默认会有缓存
     *
     * @param relativeFileName
     * @return
     */
    public InputStream getFileInputStreamInCache(String relativeFileName) {
        InputStream fileInputStream = null;
        try {
            URL url = EnvironmentSupport.class.getResource(relativeFileName);
            if (url != null) {
                fileInputStream = EnvironmentSupport.class.getResourceAsStream(relativeFileName);
            }
            if (fileInputStream == null) {
                fileInputStream = new FileInputStream(new File(relativeFileName));
            }
        } catch (Exception e) {
            logger.error("input stream error", e);
        }
        return fileInputStream;
    }
}
