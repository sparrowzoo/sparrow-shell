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

package com.sparrow.support;

import com.sparrow.enums.Order;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator<T, V extends Comparable<V>> implements Comparator<T> {
    private Map<T, V> map;

    private Order order;

    public MapValueComparator(Map<T, V> map, Order order) {
        this.map = map;
        this.order = order;
    }

    public MapValueComparator(Map<T, V> map) {
        this.map = map;
        order = Order.DESC;
    }

    @Override
    public int compare(T a, T b) {
        int result = this.map.get(a).compareTo(this.map.get(b));
        if (result == 0) {
            //按value 排序，保证数据不丢
            return this.order.equals(Order.ASC) ? 1 : -1;
        }
        return this.order.equals(Order.ASC) ? result : -result;
    }
}
