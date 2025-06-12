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

package com.sparrow.utility;

import com.sparrow.cg.PropertyNamer;
import com.sparrow.protocol.FieldOrder;
import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.constant.magic.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtility {
    private static Logger logger = LoggerFactory.getLogger(ClassUtility.class);

    public static List<Field> extractFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    public static String getBeanNameByClass(Class<?> clazz) {
        return getBeanNameByClass(clazz.getName());
    }

    public static String getBeanNameByClass(String clazzName) {
        if (clazzName.contains(".")) {
            clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1);
        }
        if (clazzName.contains("/")) {
            clazzName = clazzName.substring(clazzName.lastIndexOf("/") + 1);
        }
        clazzName = StringUtility.setFirstByteLowerCase(clazzName);
        if (clazzName.endsWith("DTO")) {
            return clazzName.substring(0, clazzName.lastIndexOf("DTO"));
        }
        if (clazzName.endsWith("VO")) {
            return clazzName.substring(0, clazzName.lastIndexOf("VO"));
        }

        if (clazzName.endsWith("BO")) {
            return clazzName.substring(0, clazzName.lastIndexOf("BO"));
        }

        if (clazzName.endsWith("Param")) {
            return clazzName.substring(0, clazzName.lastIndexOf("Param"));
        }

        if (clazzName.endsWith("Query")) {
            return clazzName.substring(0, clazzName.lastIndexOf("Query"));
        }
        return clazzName;
    }

    /**
     * @param c 接口
     * @return 实现接口的所有类
     */
    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> clazzList = new ArrayList<Class>();
        if (!c.isInterface()) {
            return clazzList;
        }
        String packageName = c.getPackage().getName();
        try {
            List<Class> allClass = getClasses(packageName);
            for (Class clazz : allClass) {
                if (!c.isAssignableFrom(clazz)) {
                    continue;
                }
                if (!c.equals(clazz)) {
                    clazzList.add(clazz);
                }
            }
        } catch (Exception ignore) {
        }
        return clazzList;
    }

    /**
     * @param packageName 包名
     * @param path        the absolute path within the classpath (never a leading slash)
     * @return a mutable Set of matching Resource instances
     * @throws ClassNotFoundException, IOException, URISyntaxException
     *                                 <p>
     *                                 參考 Find all class location resources with the given path via the ClassLoader.
     *                                 Called by {@link #findAllClassPathResources(String)}.
     * @since 4.1.1
     * <p>
     *
     * <pre>
     * protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
     * Set<Resource> result = new LinkedHashSet<>(16);
     * ClassLoader cl = getClassLoader();
     * Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
     * while (resourceUrls.hasMoreElements()) {
     * URL url = resourceUrls.nextElement();
     * result.add(convertClassLoaderURL(url));
     * }
     * if (!StringUtils.hasLength(path)) {
     * // The above result is likely to be incomplete, i.e. only containing file system references.
     * // We need to have pointers to each of the jar files on the classpath as well...
     * addAllClassLoaderJarRoots(cl, result);
     * }
     * return result;
     * }
     * </pre>
     */
    public static List<Class> getClasses(
            String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(Symbol.DOT, Symbol.SLASH);
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<Class> classes = new ArrayList<Class>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if ("file".equalsIgnoreCase(resource.getProtocol())) {
                File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name()));
                classes.addAll(findClass(directory, packageName));
            } else if ("jar".equalsIgnoreCase(resource.getProtocol())) {
                classes.addAll(findClass(((JarURLConnection) resource.openConnection())
                        .getJarFile(), path));
            }
        }
        return classes;
    }

    private static List<Class<?>> findClass(JarFile jarFile, String packagePath) {
        try {
            List<Class<?>> classes = new ArrayList<>();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().startsWith(packagePath) && jarEntry.getName().endsWith(".class")) {
                    String classFullName = jarEntry.getName().replace(Symbol.SLASH, Symbol.DOT)
                            .substring(0, jarEntry.getName().indexOf(Symbol.DOT));
                    try {
                        Class<?> implClass = Class.forName(classFullName);
                        if (!implClass.isInterface()) {
                            classes.add(implClass);
                        }
                    } catch (Throwable e) {
                        logger.error("you can ignore this error, class {} init", classFullName);
                    }
                }
            }
            return classes;
        } catch (Exception e) {
            logger.error("find class exception packagePath {}", packagePath);
            return null;
        }
    }

    private static List<Class<?>> findClass(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (directory == null || !directory.exists()) {
            return null;
        }
        File[] fileList = directory.listFiles();
        if (CollectionsUtility.isNullOrEmpty(fileList)) {
            return null;
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                classes.addAll(findClass(file, packageName + Symbol.DOT + file.getName()));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                String clazzName = packageName + Symbol.DOT + file.getName().substring(0, file.getName().length() - 6);
                logger.info("class {} init .....", clazzName);
                try {
                    classes.add(Class.forName(clazzName));
                } catch (Throwable e) {
                    logger.error("you can ignore this error, class {} init", clazzName);
                }
            }
        }

        return classes;
    }

    public static String getWrapClass(Class<?> basicType) {
        if (basicType.equals(int.class) || basicType.equals(Integer.class)) {
            return Integer.class.getName();
        }
        if (basicType.equals(byte.class) || basicType.equals(Byte.class)) {
            return Byte.class.getName();
        }
        if (basicType.equals(short.class) || basicType.equals(Short.class)) {
            return Short.class.getName();
        }
        if (basicType.equals(long.class) || basicType.equals(Long.class)) {
            return Long.class.getName();
        }
        if (basicType.equals(float.class) || basicType.equals(Float.class)) {
            return Float.class.getName();
        }
        if (basicType.equals(double.class) || basicType.equals(Double.class)) {
            return Double.class.getName();
        }
        if (basicType.equals(char.class) || basicType.equals(Character.class)) {
            return Character.class.getName();
        }
        if (basicType.equals(boolean.class) || basicType.equals(Boolean.class)) {
            return Boolean.class.getName();
        }
        if (basicType.equals(String.class)) {
            return String.class.getName();
        }
        if (basicType.equals(String[].class)) {
            return String.class.getName() + " []";
        }
        return basicType.getName();
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignore) {
        }
        if (cl != null) {
            return cl;
        }
        cl = ClassUtility.class.getClassLoader();
        if (cl != null) {
            return cl;
        }
        try {
            cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ignore) {
        }
        return cl;
    }

    static class MethodWithRank implements Comparable<MethodWithRank> {

        private Method method;
        private Float order;

        MethodWithRank(Method method, Float order) {
            this.method = method;
            this.order = order;
        }

        @Override
        public int compareTo(MethodWithRank o) {
            return this.order.compareTo(o.order);
        }
    }

    static class FieldWithRank implements Comparable<FieldWithRank> {

        private Field field;
        private Float order;

        FieldWithRank(Field field, Float order) {
            this.field = field;
            this.order = order;
        }

        @Override
        public int compareTo(FieldWithRank o) {
            return this.order.compareTo(o.order);
        }
    }

    public static Field[] getOrderedFields(Collection<Field> fields) {
        List<FieldWithRank> fieldWithRanks = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(FieldOrder.class) != null) {
                Float order = field.getAnnotation(FieldOrder.class).order();
                fieldWithRanks.add(new FieldWithRank(field, order));
            } else if (field.isAnnotationPresent(Column.class)) {
                fieldWithRanks.add(new FieldWithRank(field, Float.MAX_VALUE));
            }
        }
        Collections.sort(fieldWithRanks);
        Field[] orderFields = new Field[fieldWithRanks.size()];
        for (int i = 0; i < fieldWithRanks.size(); i++) {
            orderFields[i] = fieldWithRanks.get(i).field;
        }
        return orderFields;
    }

    public static Method[] getOrderedMethod(Method[] methods) {
        List<MethodWithRank> methodList = new ArrayList<>();
        for (Method m : methods) {
            if (m.getAnnotation(MethodOrder.class) != null) {
                Float order = m.getAnnotation(MethodOrder.class).order();
                methodList.add(new MethodWithRank(m, order));
            } else if (m.isAnnotationPresent(Column.class)) {
                methodList.add(new MethodWithRank(m, Float.MAX_VALUE));
            }
        }
        Collections.sort(methodList);
        Method[] orderMethodArray = new Method[methodList.size()];

        for (int i = 0; i < methodList.size(); i++) {
            orderMethodArray[i] = methodList.get(i).method;
        }
        return orderMethodArray;
    }

    public static class PropertyWithEntityName {
        private String propertyName;
        private String entityName;

        public PropertyWithEntityName(String propertyName, String entityName) {
            this.propertyName = propertyName;
            this.entityName = entityName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getEntityName() {
            return entityName;
        }

        public String entityDotProperty() {
            return entityName + Symbol.DOT + propertyName;
        }
    }

    public static PropertyWithEntityName getPropertyNameAndClassName(Function<?, ?> function) {
        try {
            // 反射获取 writeReplace 方法
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            // 调用该方法获取 SerializedLambda 对象
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            // 解析方法名
            String methodName = serializedLambda.getImplMethodName();
            String clazz = serializedLambda.getImplClass();
            String modelName = getBeanNameByClass(clazz);
            return new PropertyWithEntityName(PropertyNamer.methodToProperty(methodName), modelName);
        } catch (Exception e) {
            throw new RuntimeException("无法解析方法名", e);
        }
    }
}
