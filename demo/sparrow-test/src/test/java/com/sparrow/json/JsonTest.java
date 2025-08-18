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

package com.sparrow.json;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;

import java.util.HashMap;

/**
 * Created by harry on 2015/5/13.
 */
public class JsonTest {
    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.hashCode());
        System.out.println(new User().hashCode());

        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder());
        User parent = new User("1", "zhangsan", null, null);
        User lisi = new User("2", "lisi", parent,
            new HashMap<String, String>() {{
                put("key1", "value1");
            }});

        Json json = JsonFactory.getProvider();
        System.out.println(json.toString(lisi));

        System.out.println(json.toString(parent));

        for (int i = 0; i < 10; i++) {
            user = json.parse(json.toString(parent), User.class);
            System.out.println(user.getUserId());
            System.out.println(user.getUserName());
        }
    }
}
