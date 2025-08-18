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

package com.sparrow.jdk.hash;

import com.sparrow.json.User;

import java.util.TreeMap;

public class AbsentTest {
    public static void main(String[] args) {
        TreeMap<String, User> map = new TreeMap<>();
        User user = map.putIfAbsent("1", new User());
        user.setUserName("Tom");
        System.out.println(map.get("1").getUserName());
    }
}
