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
import com.sparrow.cache.CacheString;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.core.TypeConverter;
import com.sparrow.protocol.POJO;
import com.sparrow.utility.StringUtility;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * Created by harry on 2018/1/26.
 */
public class RedisCacheString extends AbstractCommand implements CacheString {
    RedisCacheString(RedisPool pool) {
        this.redisPool = pool;
    }

    @Override
    public Boolean setIfNotExistWithMills(Key key, Object value, long expireMills) throws CacheConnectionException {
        return redisPool.execute(new Executor<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                String v = typeConverter.convert(value).toString();
                SetParams setParams = new SetParams();
                setParams.nx().px(expireMills);
                return "OK".equalsIgnoreCase(jedis.set(key.key(), v, setParams));
            }
        }, key);
    }

    @Override
    public String set(final Key key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(Jedis jedis) {
                TypeConverter typeConverter = new TypeConverter(String.class);
                String v = typeConverter.convert(value).toString();
                return jedis.set(key.key(), v);
            }
        }, key);
    }

    @Override
    public String getSet(Key key, Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(Jedis jedis) throws CacheConnectionException {
                TypeConverter typeConverter = new TypeConverter(String.class);
                String v = typeConverter.convert(value).toString();
                return jedis.getSet(key.key(), v);
            }
        }, key);
    }

    @Override
    public String get(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(Jedis jedis) throws CacheConnectionException {
                return jedis.get(key.key());
            }
        }, key);
    }

    @Override
    public String get(final Key key, final CacheDataNotFound<String> hook) {
        try {
            return redisPool.execute(new Executor<String>() {
                @Override
                public String execute(Jedis jedis) {
                    String value = jedis.get(key.key());
                    if (StringUtility.isNullOrEmpty(value)) {
                        value = hook.read(key);
                        try {
                            RedisCacheString.this.set(key, value);
                        } catch (CacheConnectionException ignore) {
                        }
                    }
                    return value;
                }
            }, key);
        } catch (CacheConnectionException e) {
            return hook.read(key);
        }
    }

    @Override
    public <T> T get(final Key key, final Class clazz, final CacheDataNotFound<T> hook) {
        try {
            return redisPool.execute(new Executor<T>() {
                @Override
                public T execute(Jedis jedis) throws CacheConnectionException {
                    String json = jedis.get(key.key());
                    if (StringUtility.isNullOrEmpty(json)) {
                        if (redisPool.getCacheMonitor() != null) {
                            redisPool.getCacheMonitor().breakdown(key);
                        }
                        T value = hook.read(key);
                        RedisCacheString.this.set(key, value);
                        return value;
                    }
                    TypeConverter typeConverter = new TypeConverter(clazz);
                    return (T) typeConverter.convert(json);
                }
            }, key);
        } catch (CacheConnectionException e) {
            if (redisPool.getCacheMonitor() != null) {
                redisPool.getCacheMonitor().breakdown(key);
            }
            return hook.read(key);
        }
    }

    @Override
    public <T> T get(final Key key, final Class clazz) throws CacheConnectionException {
        return redisPool.execute(new Executor<T>() {
            @Override
            public T execute(Jedis jedis) throws CacheConnectionException {
                String json = jedis.get(key.key());
                if (StringUtility.isNullOrEmpty(json)) {
                    return null;
                }
                if (POJO.class.isAssignableFrom(clazz)) {
                    return (T) jsonProvider.parse(json, clazz);
                }
                TypeConverter typeConverter = new TypeConverter(clazz);
                return (T) typeConverter.convert(json);
            }
        }, key);
    }

    @Override
    public Long append(final Key key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.append(key.key(), value.toString());
            }
        }, key);
    }

    @Override
    public Long decrease(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.decr(key.key());
            }
        }, key);
    }

    @Override
    public Long decrease(final Key key, final Long count) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.decrBy(key.key(), count);
            }
        }, key);
    }

    @Override
    public Long increase(final Key key, final Long count) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.incrBy(key.key(), count);
            }
        }, key);
    }

    @Override
    public Long increase(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.incr(key.key());
            }
        }, key);
    }

    @Override
    public boolean bit(final Key key, final Integer offset) throws CacheConnectionException {
        return redisPool.execute(new Executor<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) {
                return jedis.getbit(key.key(), offset);
            }
        }, key);
    }

    @Override
    public String setExpire(final Key key, final Integer seconds, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<String>() {
            @Override
            public String execute(Jedis jedis) {
                return jedis.setex(key.key(), seconds, value.toString());
            }
        }, key);
    }

    @Override
    public Boolean setIfNotExist(final Key key, final Object value) throws CacheConnectionException {
        return redisPool.execute(new Executor<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) {
                return jedis.setnx(key.key(), value.toString()) > 0;
            }
        }, key);
    }
}
