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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleProxyBeanRegistry implements FactoryBean<MethodAccessor> {
    private Map<String, MethodAccessor> map = new HashMap<>();

    @Override public void pubObject(String name, MethodAccessor o) {
        this.map.put(name, o);
    }

    @Override public MethodAccessor getObject(String name) {
        return this.map.get(name);
    }

    @Override public Class<?> getObjectType() {
        return MethodAccessor.class;
    }

    @Override public void removeObject(String name) {
        this.map.remove(name);
    }

    @Override public Iterator<String> keyIterator() {
        return this.map.keySet().iterator();
    }
}
