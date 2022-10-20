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

package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.cg.User3;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.impl.SparrowContainer;

import java.lang.reflect.Method;

/**
 * @author by harry
 */
public class ReflectPerformanceTest {

    public static void main(String[] args) throws Exception {
        Container container = new SparrowContainer();
        container.init(new ContainerBuilder());


        User3 user = new User3("zhangsan");
        ((SparrowContainer) container).initProxyBean(User3.class);
        MethodAccessor methodAccessor = container.getProxyBean(User3.class);
        methodAccessor.set(user, "user", "lisi");
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            //user.getUser();
            Object userName = methodAccessor.get(user, "user");
            //System.out.printf(userName.toString());
        }
        System.out.println(System.currentTimeMillis() - t);

        t = System.currentTimeMillis();
        Method method = User3.class.getMethod("getUser");
        for (int i = 0; i < 10000000; i++) {
            method.invoke(user);
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}
