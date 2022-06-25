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

package com.sparrow.core.cache;

import com.sparrow.container.FactoryBean;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheRegistry implements FactoryBean<Cache> {
    private CacheRegistry() {
    }

    private static class Nested {
        private static CacheRegistry instant = new CacheRegistry();
    }

    public static CacheRegistry getInstance() {
        return Nested.instant;
    }

    private Map<String, Cache> registry = new ConcurrentHashMap<>();

    @Override
    public void pubObject(String name, Cache o) {
        registry.put(name, o);
    }

    @Override
    public Cache getObject(String name) {
        return registry.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    public void removeObject(String name) {
        registry.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return registry.keySet().iterator();
    }
}
