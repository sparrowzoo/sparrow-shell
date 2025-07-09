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

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class EnumUtility {

    public static List<KeyValue<Integer, String>> getNames(String fullEnumName) throws ClassNotFoundException {
        Class<?> c = Class.forName(fullEnumName);
        return getNames(c);
    }

    public static List<KeyValue<Integer, String>> getNames(Class<?> e) {
        List<KeyValue<Integer, String>> kvs = new ArrayList<>();
        Class<Enum<?>> c = (Class<Enum<?>>) e;
        Enum<?>[] enums = c.getEnumConstants();
        for (Enum<?> en : enums) {
            String name = en.name();
            Integer key = en.ordinal();
            if (en instanceof EnumIdentityAccessor) {
                key = ((EnumIdentityAccessor) en).getIdentity();
            }
            kvs.add(new KeyValue<>(key, name));
        }
        return kvs;
    }
}
