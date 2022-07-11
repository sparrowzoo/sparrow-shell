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

package com.sparrow.web.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodParameter {
    private final Method method;
    private final int parameterIndex;
    private volatile Class<?> parameterType;
    private volatile String parameterName;
    private Class<?>[] interfaces;

    public MethodParameter(Method method, int parameterIndex, Class<?> parameterType, String parameterName,
        Annotation[] parameterAnnotations) {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.parameterType = parameterType;
        this.parameterName = parameterName;
        if (this.parameterType != null) {
            this.interfaces = this.parameterType.getInterfaces();
        }
    }

    public MethodParameter(Method method, int parameterIndex, Class<?> parameterType, String parameterName) {
        this(method, parameterIndex, parameterType, parameterName, null);
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }
}
