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
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class Generator4MethodAccessorImpl implements Generator4MethodAccessor {
    private static Logger logger = LoggerFactory.getLogger(Generator4MethodAccessor.class);

    private static final String PACKAGE_NAME = "com.sparrow.accessMethod";

    @Override
    public MethodAccessor newMethodAccessor(Class<?> clazz) {
        StringBuilder setJavaSource = new StringBuilder();
        StringBuilder getJavaSource = new StringBuilder();

        String operatorClassName = clazz.getName();
        String operatorObjectName = StringUtility.setFirstByteLowerCase(clazz
            .getSimpleName());

        String methodAccessorClassName = clazz.getSimpleName() + "MethodAccess";
        getJavaSource.append("public Object get(Object o, String methodName){");
        getJavaSource.append(Constant.ENTER_TEXT);
        getJavaSource.append(String.format("%1$s %2$s=(%1$s)o;",
            operatorClassName, operatorObjectName));
        setJavaSource
            .append("public void set(Object o, String methodName,Object arg){");
        setJavaSource.append(Constant.ENTER_TEXT);
        setJavaSource.append(String.format("%1$s %2$s=(%1$s)o;",
            operatorClassName, operatorObjectName));

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getDeclaringClass().equals(Object.class)) {
                continue;
            }
            Class<?>[] parameterType = method.getParameterTypes();
            if (PropertyNamer.isSetter(method.getName()) && parameterType.length == 1) {
                setJavaSource.append(Constant.ENTER_TEXT);
                setJavaSource.append(String.format(
                    "if(methodName.equalsIgnoreCase(\"%1$s\")||methodName.equalsIgnoreCase(\"%2$s\")){",
                    method.getName(),
                    PropertyNamer.methodToProperty(method.getName())));

                setJavaSource.append(String.format("%1$s.%2$s((%3$s)arg);}",
                    operatorObjectName, method.getName(),
                    ClassUtility.getWrapClass(parameterType[0])));
                setJavaSource.append(Constant.ENTER_TEXT);

            } else if (PropertyNamer.isGetter(method.getName())
                && parameterType.length == 0
                && !method.getReturnType().equals(void.class)) {
                getJavaSource.append(Constant.ENTER_TEXT);
                getJavaSource.append(String.format(
                    "if(methodName.equalsIgnoreCase(\"%1$s\")||methodName.equalsIgnoreCase(\"%2$s\")){", method.getName().toLowerCase(), PropertyNamer.methodToProperty(method.getName()).toLowerCase()));
                getJavaSource.append(String.format("return %1$s.%2$s();}",
                    operatorObjectName, method.getName()));
                getJavaSource.append(Constant.ENTER_TEXT);
            }
        }
        setJavaSource.append("}");
        setJavaSource.append(Constant.ENTER_TEXT);
        getJavaSource.append("return null;");
        getJavaSource.append("}");
        getJavaSource.append(Constant.ENTER_TEXT);
        String sourceCode = "package " + PACKAGE_NAME + ";" + Constant.ENTER_TEXT
            + "import com.sparrow.cg.MethodAccessor;"
            + Constant.ENTER_TEXT + " public class "
            + methodAccessorClassName + "  implements MethodAccessor{"
            + Constant.ENTER_TEXT + getJavaSource.toString()
            + setJavaSource.toString() + "}";
        if (ConfigUtility.getBooleanValue(Config.DEBUG_METHOD_ACCESS, false)) {
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
