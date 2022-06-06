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

import java.util.HashMap;
import java.util.Map;

/**
 * HashMapDemo
 *
 * @author harry
 */
public class HashMapDemo {
    public static void main(String[] args) {
        int initialCapacity = 7;
        //权衡空间与时间复杂度因子
        float loadFactor = 0.75f;
        Map map = new HashMap(initialCapacity, loadFactor);
        for (int i = 0; i < 15; i++) {
            map.put(i, 2);
        }
        //resize
        map.put(15, 2);
        //2的n 次方>=initialCapacity的最小值
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;
        System.out.println(capacity);
    }
}
