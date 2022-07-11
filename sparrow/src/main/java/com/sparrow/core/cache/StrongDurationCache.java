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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StrongDurationCache<K, V> extends AbstractCache<K, V> {
    private Map<K, V> cache = new ConcurrentHashMap<>();

    public StrongDurationCache(String name) {
        super(name);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<K, V> map) {
        cache.putAll(map);
    }

    @Override
    public V getIfPresent(K key) {
        return cache.get(key);
    }

    @Override
    public Map<K, V> getAllPresent(Iterable<K> keys) {
        Map<K, V> map = new HashMap<>();
        for (K k : keys) {
            map.put(k, cache.get(k));
        }
        return map;
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return new ConcurrentHashMap<>(cache);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void remove(K name) {
        cache.remove(name);
    }
}
