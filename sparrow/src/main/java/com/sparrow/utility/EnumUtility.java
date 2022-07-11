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

import com.sparrow.protocol.constant.magic.DIGIT;
import com.sparrow.protocol.constant.magic.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumUtility {

    /**
     * 将枚举转成map
     */
    public static Map<String, String> getMap(Class<?> e, int maxCount, boolean nameAsKey) {
        if (maxCount <= 0) {
            maxCount = Integer.MAX_VALUE;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        Class<Enum<?>> c = (Class<Enum<?>>) e;
        Enum<?>[] enums = c.getEnumConstants();
        for (Enum<?> en : enums) {
            if (map.size() < maxCount) {
                String key = nameAsKey ? en.name() : String.valueOf(en.ordinal());
                String value = getValue(en);
                if (StringUtility.isNullOrEmpty(value)) {
                    map.put(key, en.toString());
                } else {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    /**
     * 前端控件使用 枚举转成map
     *
     * @param className com.sparrow.enums.Status:10:true
     */
    public static Map<String, String> getMap(String className) {
        int maxCount = Integer.MAX_VALUE;
        String[] classArray = className.split("\\:");
        Class<?> e;
        boolean nameAsKey = false;
        try {
            className = classArray[0];
            if (!className.contains(".")) {
                className = "com.sparrow.protocol.enums." + className;
            }
            e = Class.forName(className);
            if (classArray.length >= DIGIT.TOW) {
                maxCount = Integer.valueOf(classArray[1]);
            }
            if (classArray.length == DIGIT.THREE) {
                nameAsKey = true;
            }
        } catch (ClassNotFoundException ignore) {
            throw new RuntimeException(ignore);
        }
        if (e != null) {
            return getMap(e, maxCount, nameAsKey);
        }
        return null;
    }

    /**
     * 获取enum对应的 key
     */
    public static String getValue(String enumClassName, int index) {
        try {
            Class clazz = Class.forName(enumClassName);
            String key = "enum" + Symbol.UNDERLINE + clazz.getSimpleName() + Symbol.UNDERLINE + clazz
                .getEnumConstants()[index].toString().toLowerCase();
            return ConfigUtility.getLanguageValue(key);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValue(Enum enumInstance) {
        String simpleName = enumInstance.getDeclaringClass().getSimpleName();
        String classKey = StringUtility.humpToLower(simpleName);
        String key = "enum" + Symbol.UNDERLINE + classKey + Symbol.UNDERLINE + enumInstance.name().toLowerCase();
        return ConfigUtility.getLanguageValue(key);
    }
}
