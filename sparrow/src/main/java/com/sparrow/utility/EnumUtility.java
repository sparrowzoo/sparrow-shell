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

import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.magic.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumUtility {
    public static Map<String, String> getNameValueMap(Class<?> e) {
        return getMap(e, true, null);
    }

    public static Map<String, String> getOrdinalValueMap(Class<?> e) {
        return getMap(e, false, null);
    }

    public static Map<String, String> getNameValueMap(Class<?> e, String business) {
        return getMap(e, true, business);
    }

    public static Map<String, String> getOrdinalValueMap(Class<?> e, String business) {
        return getMap(e, false, business);
    }

    /**
     * 将枚举转成map
     */
    private static Map<String, String> getMap(Class<?> e, boolean nameAsKey, String business) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Class<Enum<?>> c = (Class<Enum<?>>) e;
        Enum<?>[] enums = c.getEnumConstants();
        for (Enum<?> en : enums) {
            String key = nameAsKey ? en.name() : String.valueOf(en.ordinal());
            String value = getValue(en, business);
            if (StringUtility.isNullOrEmpty(value)) {
                map.put(key, en.toString());
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 获取enum对应的 key
     */
    public static String getValue(Class<?> clazz, int index, String business) {
        String classKey = StringUtility.humpToLower(clazz.getSimpleName());
        Enum<?> enumInstance = (Enum<?>) clazz
                .getEnumConstants()[index];
        String enumKey = enumInstance.toString().toLowerCase();
        String key = "enum" + Symbol.UNDERLINE + classKey + Symbol.UNDERLINE + enumKey;
        if (!StringUtility.isNullOrEmpty(business)) {
            key += Symbol.UNDERLINE + business;
        }
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        String languageValue = configReader.getI18nValue(key);
        return StringUtility.isNullOrEmpty(languageValue) ? enumInstance.name() : languageValue;
    }

    public static String getValue(Enum<?> enumInstance, String business) {
        String simpleName = enumInstance.getDeclaringClass().getSimpleName();
        String classKey = StringUtility.humpToLower(simpleName);
        String key = "enum" + Symbol.UNDERLINE + classKey + Symbol.UNDERLINE + enumInstance.name().toLowerCase();
        if (!StringUtility.isNullOrEmpty(business)) {
            key += Symbol.UNDERLINE + business;
        }
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        String languageValue = configReader.getI18nValue(key);
        return StringUtility.isNullOrEmpty(languageValue) ? enumInstance.name() : languageValue;
    }
}
