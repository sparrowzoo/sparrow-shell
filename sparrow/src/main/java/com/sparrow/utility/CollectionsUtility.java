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

import com.sparrow.enums.Order;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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


    /**
     * 非线程安全，注册上层加锁控制
     *
     * @param linkedHashMap
     * @param key
     * @param limit
     * @param <K>
     */
    public static <K> void incrementByKey(LinkedHashMap<K, AtomicLong> linkedHashMap, K key, Integer limit) {
        if (linkedHashMap == null) {
            return;
        }
        if (linkedHashMap.containsKey(key)) {
            linkedHashMap.get(key).incrementAndGet();
            return;
        }
        linkedHashMap.put(key, new AtomicLong(1));
        if (linkedHashMap.size() > limit) {
            //移掉第一个
            Iterator<Map.Entry<K, AtomicLong>> it = linkedHashMap.entrySet().iterator();
            if (it.hasNext()) {
                it.next();
                it.remove();
            }
        }
    }

    /**
     * 相较于MapValueComparator 更安全
     * <p>
     * 而且即使取top 也需要将所有对象put进treemap
     * 效率相当
     *
     * @param map
     * @param order
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<V>> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map, Order order) {
        if (map == null) {
            return null;
        }
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (order.equals(Order.ASC)) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list;
    }

    public static <K, V> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map, Order order, Comparator<V> comparator) {
        if (map == null) {
            return null;
        }
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (order.equals(Order.ASC)) {
                return comparator.compare(o1.getValue(), o2.getValue());
            }
            return comparator.compare(o2.getValue(), o1.getValue());
        });
        return list;
    }
}
