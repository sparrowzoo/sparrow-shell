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

package com.sparrow.facade.utility;

import com.sparrow.utility.RandomUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * LuckyDraw
 *
 * @author harry
 */
public class LuckyDraw {
    public static void main(String[] args) {
        Map<String, Double> rules = new HashMap<String, Double>();
        rules.put("4", 0.5d);
        rules.put("2", 0.3d);
        rules.put("3", 0.15d);
        rules.put("1", 0.05d);
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (int i = 0; i <= 1000; i++) {
            String label = RandomUtility.lottery(rules, 1000, "null");
            if (countMap.containsKey(label)) {
                countMap.put(label, countMap.get(label) + 1);
            } else {
                countMap.put(label, 1);
            }
        }
        for (String label : countMap.keySet()) {
            System.out.println(label);
            System.out.println(countMap.get(label));
        }
    }
}
