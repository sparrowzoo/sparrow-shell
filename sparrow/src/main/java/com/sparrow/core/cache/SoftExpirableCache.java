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

import com.sparrow.concurrent.SparrowThreadFactory;
import com.sparrow.constant.DateTime;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SoftExpirableCache<K, V> extends AbstractCache<K, V> implements ExpirableCache<K, V> {

    private Map<K, SoftReference<ExpirableData<V>>> expirableMap = new ConcurrentHashMap<>();

    private int expireSeconds;

    public SoftExpirableCache(String name, int expire) {
        super(name);

        this.expireSeconds = expire;

        ScheduledThreadPoolExecutor cleaner = new ScheduledThreadPoolExecutor(1, new SparrowThreadFactory.Builder().namingPattern("cache-expire-cleaner").build());
        cleaner.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Map<K, SoftReference<ExpirableData<V>>> expirableDataMap = SoftExpirableCache.this.expirableMap;
                if (expirableDataMap == null || expirableDataMap.size() == 0) {
                    return;
                }

                for (K key : expirableDataMap.keySet()) {
                    SoftReference<ExpirableData<V>> expirableData = expirableDataMap.get(key);
                    if (expirableData == null) {
                        continue;
                    }
                    ExpirableData<V> data = expirableData.get();
                    if (data == null) {
                        continue;
                    }
                    if (data.isExpire()) {
                        expirableDataMap.remove(key);
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override public V get(K key) {
        return this.get(key, null, this.expireSeconds);
    }

    @Override public void put(K key, V value) {
        this.put(key, value, this.expireSeconds);
    }

    @Override public void putAll(Map<K, V> map) {
        for (K k : map.keySet()) {
            this.put(k, map.get(k), this.expireSeconds);
        }
    }

    @Override public V getIfPresent(K key) {
        return this.get(key, null, this.expireSeconds);
    }

    @Override public Map<K, V> getAllPresent(Iterable keys) {
        return null;
    }

    @Override public long size() {
        return this.expirableMap.size();
    }

    @Override public ConcurrentMap<K, V> asMap() {
        ConcurrentMap<K, V> map = new ConcurrentHashMap<>();
        for (K key : this.expirableMap.keySet()) {
            SoftReference<ExpirableData<V>> value = this.expirableMap.get(key);
            if (value == null) {
                continue;
            }
            ExpirableData<V> expirableData = value.get();
            if (expirableData == null) {
                continue;
            }
            if (expirableData.isExpire()) {
                continue;
            }
            map.put(key, expirableData.getData());
        }
        return map;
    }

    @Override public void clear() {
        this.expirableMap.clear();
    }

    @Override public void remove(K name) {
        this.expirableMap.remove(name);
    }

    @Override public void invalidate(K key) {
        SoftReference<ExpirableData<V>> data = this.expirableMap.get(key);
        if (data != null) {
            ExpirableData<V> expirableData = data.get();
            if (expirableData != null) {
                expirableData.setTimestamp(DateTime.MIN_UNIX_TIMESTAMP.getTime());
            }
        }
    }

    @Override public void invalidateAll(Iterable<K> keys) {
        for (K key : keys) {
            this.invalidate(key);
        }
    }

    @Override public void invalidateAll() {
        for (K key : this.expirableMap.keySet()) {
            this.invalidate(key);
        }
    }

    @Override public void put(K key, V value, int expire) {
        this.expirableMap.put(key, new SoftReference<>(new ExpirableData<V>(expire, value)));
    }

    public V get(K key, LocalCacheNotFound<K, V> hook, int expire) {
        SoftReference<ExpirableData<V>> softReference = this.expirableMap.get(key);
        if (softReference != null) {
            return softReference.get().getData();
        }
        if (hook == null) {
            return null;
        }
        V o = hook.read(key);
        //if null cache shorten expire time
        expire = (o == null) ? (int) Math.ceil(expire / 10D) : expire;
        ExpirableData<V> e = new ExpirableData<>(expire, o);
        this.expirableMap.put(key, new SoftReference<>(e));
        return o;
    }

    @Override
    public void continueKey(K key) {
        SoftReference<ExpirableData<V>> data = this.expirableMap.get(key);
        if (data != null) {
            ExpirableData<V> expirableData = data.get();
            if (expirableData != null) {
                expirableData.setTimestamp(System.currentTimeMillis());
            }
        }
    }
}
