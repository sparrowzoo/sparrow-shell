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

package com.sparrow.cache.impl.redis.jedis;

import com.sparrow.cache.CacheDataNotFound;
import com.sparrow.cache.CacheList;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.core.TypeConverter;
import com.sparrow.utility.StringUtility;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.Jedis;

/**
 * Created by harry on 2018/1/26.
 */
public class RedisCacheList extends AbstractCommand implements CacheList {
    RedisCacheList(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    @Override
    public Long getSize(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.llen(key.key());
            }
        }, key);
    }

    @Override
    public <T> Long add(final Key key, final T value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                return jedis.rpush(key.key(), typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public Long add(final Key key, final String... values) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.lpush(key.key(), values);
            }
        }, key);
    }

    @Override
    public <T> Integer add(final Key key, final Iterable<T> values) throws CacheConnectionException {
        return redisPool.execute(new Executor<Integer>() {
            @Override
            public Integer execute(Jedis jedis) {
                String[] valueArray = StringUtility.getStringArray(values);
                jedis.lpush(key.key(), valueArray);
                return valueArray.length;
            }
        }, key);
    }

    @Override
    public <T> Long remove(final Key key, final T value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                TypeConverter typeConverter = new TypeConverter(value.getClass());
                return jedis.lrem(key.key(), 1L, typeConverter.convert(value).toString());
            }
        }, key);
    }

    @Override
    public List<String> list(final Key key) throws CacheConnectionException {
        return this.list(key, String.class);
    }

    @Override
    public <T> List<T> list(final Key key, final Class clazz) throws CacheConnectionException {
        return redisPool.execute(new Executor<List<T>>() {
            @Override
            public List<T> execute(Jedis jedis) throws CacheConnectionException {
                List<String> list = jedis.lrange(key.key(), 0, -1);
                List<T> tList = new ArrayList<T>(list.size());
                TypeConverter typeConverter = new TypeConverter(clazz);
                for (String s : list) {
                    tList.add((T) typeConverter.convert(s));
                }
                return tList;
            }
        }, key);
    }

    @Override
    public List<String> list(final Key key, CacheDataNotFound<List<String>> hook) {
        return this.list(key, String.class, hook);
    }

    @Override
    public <T> List<T> list(final Key key, final Class clazz, final CacheDataNotFound<List<T>> hook) {
        try {
            return redisPool.execute(new Executor<List<T>>() {
                @Override
                public List<T> execute(Jedis jedis) throws CacheConnectionException {
                    List<String> list = jedis.lrange(key.key(), 0, -1);
                    List<T> typeList = null;
                    if (list == null || list.size() == 0) {
                        if (redisPool.getCacheMonitor() != null) {
                            redisPool.getCacheMonitor().breakdown(key);
                        }
                        typeList = hook.read(key);
                        RedisCacheList.this.add(key, list);
                        return typeList;
                    }
                    typeList = new ArrayList<T>(list.size());
                    TypeConverter typeConverter = new TypeConverter(clazz);
                    for (String s : list) {
                        typeList.add((T) typeConverter.convert(s));
                    }
                    return typeList;
                }
            }, key);
        } catch (CacheConnectionException e) {
            if (redisPool.getCacheMonitor() != null) {
                redisPool.getCacheMonitor().breakdown(key);
            }
            return hook.read(key);
        }
    }
}
