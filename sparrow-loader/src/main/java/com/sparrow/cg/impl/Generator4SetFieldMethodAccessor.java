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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Generator4SetFieldMethodAccessor implements Generator4MethodAccessor {
    private static Logger logger = LoggerFactory.getLogger(Generator4MethodAccessor.class);

    static class FieldSetMethodAccessor implements MethodAccessor {
        private Map<String, Field> fieldMap = new HashMap<>();

        public FieldSetMethodAccessor(Class clazz) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field);
            }
            if (clazz.getSuperclass() != null) {
                Field[] fields = clazz.getSuperclass().getDeclaredFields();
                if (fields.length > 0) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        fieldMap.put(field.getName(), field);
                    }
                }
            }
        }

        @Override public Object get(Object o, String methodName) {
            Field field = fieldMap.get(methodName);
            if (field == null) {
                logger.warn("field not found {} of object class name {}", methodName, o.getClass().getName());
                return null;
            }
            try {
                return field.get(o);
            } catch (IllegalAccessException e) {
                logger.warn("field get error {} of object class name {}", methodName, o.getClass().getName());
                return null;
            }
        }

        @Override public void set(Object o, String methodName, Object arg) {
            Field field = fieldMap.get(methodName);
            if (field == null) {
                logger.warn("field not found {} of object class name {}", methodName, o.getClass().getName());
                return;
            }
            try {
                field.set(o, arg);
            } catch (IllegalAccessException e) {
                logger.error("set field error field-name={}", field.getName(), e);
            }
        }
    }

    @Override
    public MethodAccessor newMethodAccessor(Class<?> clazz) {
        return new FieldSetMethodAccessor(clazz);
    }
}
