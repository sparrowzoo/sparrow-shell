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

import com.sparrow.cg.Generator4MethodAccessor;
import com.sparrow.cg.MethodAccessor;
import com.sparrow.cg.PropertyNamer;
import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class Generator4MethodHashAccessor implements Generator4MethodAccessor {
    private static Logger logger = LoggerFactory.getLogger(Generator4MethodAccessor.class);
    private static final String PACKAGE_NAME = "com.sparrow.accessMethod";

    @Override
    public MethodAccessor newMethodAccessor(Class<?> clazz) {
        StringBuilder setJavaSource = new StringBuilder();
        StringBuilder setHashJavaSource = new StringBuilder();
        StringBuilder getJavaSource = new StringBuilder();
        StringBuilder getHashJavaSource = new StringBuilder();

        String operatorClassName = clazz.getName();
        String operatorObjectName = StringUtility.setFirstByteLowerCase(clazz
                .getSimpleName());

        String map = String.format("private static Map<String, BiFunction<%1$s, String, Object>> getMap = new HashMap<>();\n" +
                "        private static Map<String, BiConsumer<%1$s, Object>> setMap = new HashMap<>();\n", operatorClassName);
        String methodAccessorClassName = clazz.getSimpleName() + "MethodAccess";
        getJavaSource.append("public Object get(Object o, String methodName){");
        getJavaSource.append(Constant.ENTER_TEXT);
        getJavaSource.append(String.format("%1$s %2$s=(%1$s)o;",
                operatorClassName, operatorObjectName));
        getJavaSource.append("return getMap.get(methodName).apply(");
        getJavaSource.append(operatorObjectName);
        getJavaSource.append(",methodName);");

        setJavaSource
                .append("public void set(Object o, String methodName,Object arg){");
        setJavaSource.append(Constant.ENTER_TEXT);
        setJavaSource.append(String.format("%1$s %2$s=(%1$s)o;",
                operatorClassName, operatorObjectName));
        setJavaSource.append(Constant.ENTER_TEXT);

        setJavaSource.append(" setMap.get(methodName).accept(");
        setJavaSource.append(operatorObjectName);
        setJavaSource.append(",arg);");

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getDeclaringClass().equals(Object.class)) {
                continue;
            }
            Class<?>[] parameterType = method.getParameterTypes();
            if (PropertyNamer.isSetter(method.getName()) && parameterType.length == 1) {
                setHashJavaSource.append(Constant.ENTER_TEXT);
                setHashJavaSource.append(String.format("setMap.put(\"%1$s\", (o, arg) -> o.%2$s((%3$s) arg));",
                        PropertyNamer.methodToProperty(method.getName()),
                        method.getName(),
                        ClassUtility.getWrapClass(parameterType[0])));

                setHashJavaSource.append(Constant.ENTER_TEXT);
            } else if (PropertyNamer.isGetter(method.getName())
                    && parameterType.length == 0
                    && !method.getReturnType().equals(void.class)) {
                getHashJavaSource.append(Constant.ENTER_TEXT);
                getHashJavaSource.append(String.format("getMap.put(\"%1$s\", (o, r) -> o.%2$s());", PropertyNamer.methodToProperty(method.getName()), method.getName()));
                getHashJavaSource.append(Constant.ENTER_TEXT);
            }
        }
        setJavaSource.append("}");
        setJavaSource.append(Constant.ENTER_TEXT);
        getJavaSource.append("}");
        getJavaSource.append(Constant.ENTER_TEXT);
        String sourceCode = "package " + PACKAGE_NAME + ";" + Constant.ENTER_TEXT
                + "import com.sparrow.cg.MethodAccessor;\n"
                + "import java.util.HashMap;\n"
                + "import java.util.Map;\n"
                + "import java.util.function.BiConsumer;\n"
                + "import java.util.function.BiFunction;\n"
                + Constant.ENTER_TEXT + " public class "
                + methodAccessorClassName + "  implements MethodAccessor{"
                + Constant.ENTER_TEXT + map
                + Constant.ENTER_TEXT + "static {"
                + Constant.ENTER_TEXT + getHashJavaSource.toString()
                + Constant.ENTER_TEXT + setHashJavaSource.toString()
                + Constant.ENTER_TEXT + "}"

                + Constant.ENTER_TEXT + getJavaSource.toString()
                + setJavaSource.toString() + "}";

        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        if (configReader.getBooleanValue(Config.METHOD_ACCESS_DEBUG, false)) {
            logger.debug(sourceCode);
        }
        try {
            return (MethodAccessor) DynamicCompiler
                    .getInstance().sourceToObject(
                            PACKAGE_NAME + "." + methodAccessorClassName,
                            sourceCode);
        } catch (Exception e) {
            logger.error("DynamicCompiler get sourceToObject error", e);
        }
        return null;
    }
}
