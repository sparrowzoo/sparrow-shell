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

import java.util.*;

public class CollectionsUtility {

    public static <T extends Comparable<T>> T getLevel(List<T> list, T currentValue) {
        T result = null;
        for (T item : list) {
            if (item.compareTo(currentValue) > 0) {
                break;
            }
            result = item;
        }
        return result;
    }

    public static Boolean isNullOrEmpty(Iterable<?> collection) {
        return collection == null || !collection.iterator().hasNext();
    }

    public static <T> Boolean isNullOrEmpty(T[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * 除去数组中的空值 <p> "sign".equalsIgnoreCase(key) || "sign_type".equalsIgnoreCase(key)
     *
     * @param array 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> filterEmpty(Map<String, String> array, String[] exceptArray) {
        Map<String, String> result = new TreeMap<String, String>();
        if (array == null || array.size() <= 0) {
            return result;
        }
        for (String key : array.keySet()) {
            String value = array.get(key);
            if (StringUtility.isNullOrEmpty(value) || StringUtility.existInArray(exceptArray, key)) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static void fillMapWithProperties(Map<String, String> map, Properties props) {
        if (props == null) {
            return;
        }
        for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
            String key = (String) en.nextElement();
            String value = props.getProperty(key);
            if (value == null) {
                // Allow for defaults fallback or potentially overridden accessor...
                value = props.getProperty(key);
            }
            map.put(key, value);
        }
    }
}
