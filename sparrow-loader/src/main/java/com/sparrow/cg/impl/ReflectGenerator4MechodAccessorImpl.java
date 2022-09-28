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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ReflectGenerator4MechodAccessorImpl implements Generator4MethodAccessor {

    @Override
    public MethodAccessor newMethodAccessor(Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> cache = new HashMap<>(methods.length * 4 / 3);
        for (Method method : methods) {
            method.setAccessible(true);
            final String methodNameLowCase = method.getName();
            cache.put(methodNameLowCase, method);
        }
        return new ReflectMethodAccessor(cache);
    }

    private static class ReflectMethodAccessor implements MethodAccessor {

        private final Map<String, Method> methodCache;

        public ReflectMethodAccessor(Map<String, Method> methodCache) {
            this.methodCache = methodCache;
        }

        @Override
        public Object get(Object o, String methodName) {
            return invoke(o, methodName);
        }

        @Override
        public void set(Object o, String methodName, Object arg) {
            invoke(o, methodName, arg);
        }

        /**
         * execute method
         *
         * @param o
         * @param methodName
         * @param args
         * @return
         */
        private Object invoke(Object o, String methodName, Object... args) {
            final Map<String, Method> cache = this.methodCache;
            final String methodNameLowerCase = methodName;
            final Method method = cache.get(methodNameLowerCase);
            final Class<?> returnType = method.getReturnType();
            try {
                if (returnType == void.class) {
                    method.invoke(o, args);
                    return null;
                }
                return method.invoke(o, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
