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
import com.sparrow.support.MapValueComparator;
import java.util.Map;
import java.util.TreeMap;

public class RandomUtility {
    /**
     * @param rules        规则为treeMap 概率小的在前边
     * @param poolSize     奖池大小
     * @param defaultValue 未中默认奖
     * @return
     */
    public static <T> T lottery(Map<T, Double> rules, int poolSize, T defaultValue) {
        Map<T, Double> valueSortedRules = new TreeMap<T, Double>(new MapValueComparator(rules, Order.ASC));
        valueSortedRules.putAll(rules);
        int randomNumber = new java.util.Random().nextInt(poolSize);
        double limit = 0.0;
        for (T label : valueSortedRules.keySet()) {
            //valueSortedRules.get(label) null exception
            limit += Math.ceil(poolSize * rules.get(label));
            if (randomNumber <= limit) {
                return label;
            }
        }
        return defaultValue;
    }
}
