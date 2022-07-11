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

package com.sparrow.cache.impl.redis;

import com.sparrow.cache.CacheDataNotFound;
import com.sparrow.cache.CacheHash;
import com.sparrow.constant.cache.KEY;
import com.sparrow.core.TypeConverter;
import com.sparrow.exception.CacheConnectionException;
import com.sparrow.utility.StringUtility;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import redis.clients.jedis.ShardedJedis;

public class RedisCacheHash extends AbstractCommand implements CacheHash {
    RedisCacheHash(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    @Override
    public Map<String, String> getAll(final KEY key) throws CacheConnectionException {
        return this.getAll(key, String.class, String.class);
    }

    @Override
    public <K, T> Map<K, T> getAll(final KEY key, final Class keyClazz, final Class clazz, final CacheDataNotFound<Map<K, T>> hook) {
        try {
            return redisPool.execute(new Executor<Map<K, T>>() {
                @Override
                public Map<K, T> execute(ShardedJedis jedis) throws CacheConnectionException {

                    Map<String, String> map = jedis.hgetAll(key.key());
                    if (map == null || map.size() == 0) {
                        if (redisPool.getCacheMonitor() != null) {
                            redisPool.getCacheMonitor().penetrate(key);
                        }
                        if (jedis.exists(key.key())) {
                            return new HashMap<>();
                        }
                        Map<K, T> result = hook.read(key);
                        if (hook.backWrite(result)) {
                            RedisCacheHash.this.put(key, result);
                        }
                        return result;
                    }
                    return assembleMap(keyClazz, clazz, map);
                }
            }, key);
        } catch (CacheConnectionException e) {
            if (redisPool.getCacheMonitor() != null) {
                redisPool.getCacheMonitor().penetrate(key);
            }
            return hook.read(key);
        }
    }

    private <K, T> Map<K, T> assembleMap(Class keyClazz, Class clazz, Map<String, String> map) {
        Map<K, T> result = new HashMap<>();
        TypeConverter valueConverter = new TypeConverter(clazz);
        TypeConverter keyTypeConverter = new TypeConverter(keyClazz);
        for (String k : map.keySet()) {
            String value = map.get(k);
            if (StringUtility.isNullOrEmpty(value)) {
                continue;
            }
            T t = (T) valueConverter.convert(value);
            result.put((K) keyTypeConverter.convert(k), t);
        }
        return result;
    }

    @Override
    public <K, T> Map<K, T> getAll(final KEY key, final Class keyClazz, final Class clazz) throws CacheConnectionException {
        return redisPool.execute(new Executor<Map<K, T>>() {
            @Override
            public Map<K, T> execute(ShardedJedis jedis) throws CacheConnectionException {
                Map<String, String> map = jedis.hgetAll(key.key());
                if (map.size() == 0) {
                    return null;
                }
                return assembleMap(keyClazz, clazz, map);
            }
        }, key);
    }

    @Override
    public Long getSize(final KEY key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) throws CacheConnectionException {
                return jedis.hlen(key.key());
            }
        }, key);
    }

    @Override
    public String get(final KEY key, final String field) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(ShardedJedis jedis) throws CacheConnectionException {
                return jedis.hget(key.key(), field);
            }
        }, key);
    }

    @Override
    public Map<String, String> get(final KEY key, final Collection<String> fieldList) throws CacheConnectionException {
        return redisPool.execute(new Executor<Map<String, String>>() {
            @Override
            public Map<String, String> execute(ShardedJedis jedis) throws CacheConnectionException {
                Map<String, String> values = new LinkedHashMap<String, String>(fieldList.size());
                for (String field : fieldList) {
                    values.put(field, jedis.hget(key.key(), field));
                }
                return values;
            }
        }, key);
    }

    @Override
    public <T> Map<String, T> get(final KEY key, final Collection<String> fieldList, final Class valueType) throws CacheConnectionException {
        return redisPool.execute(new Executor<Map<String, T>>() {
            @Override
            public Map<String, T> execute(ShardedJedis jedis) throws CacheConnectionException {
                Map<String, T> values = new LinkedHashMap<String, T>(fieldList.size());
                for (String field : fieldList) {
                    String value = jedis.hget(key.key(), field);
                    values.put(field, (T) new TypeConverter(valueType).convert(value));
                }
                return values;
            }
        }, key);
    }

    @Override
    public <T> T get(final KEY key, final String field, final Class valueType) throws CacheConnectionException {
        return redisPool.execute(new Executor<T>() {
            @Override
            public T execute(ShardedJedis jedis) throws CacheConnectionException {
                String value = jedis.hget(key.key(), field);
                if (StringUtility.isNullOrEmpty(value)) {
                    return null;
                }
                return (T) new TypeConverter(valueType).convert(value);
            }
        }, key);
    }

    @Override
    public <T> T get(KEY key, String field, Class valueClass, CacheDataNotFound<T> hook) {
        try {
            return redisPool.execute(jedis -> {
                String value = jedis.hget(key.key(), field);
                if (StringUtility.isNullOrEmpty(value)) {
                    return hook.read(key);
                }
                return (T) new TypeConverter(valueClass).convert(value);
            }, key);
        } catch (CacheConnectionException e) {
            return hook.read(key);
        }
    }

    @Override
    public Long put(final KEY key, final String field, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(ShardedJedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                return jedis.hset(key.key(), field, typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public <K, T> Integer put(final KEY key, final Map<K, T> map) throws CacheConnectionException {
        return redisPool.execute(new Executor<Integer>() {
            @Override
            public Integer execute(ShardedJedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                Map<String, String> newMap = new HashMap<>();
                for (K k : map.keySet()) {
                    newMap.put(typeConverter.convert(k).toString(), typeConverter.convert(map.get(k)).toString());
                }
                jedis.hmset(key.key(), newMap);
                return map.size();
            }
        }, key);
    }
}
