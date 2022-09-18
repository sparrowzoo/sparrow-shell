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

import com.sparrow.cache.CacheKey;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import redis.clients.jedis.Jedis;

public class RedisCacheKey extends AbstractCommand implements CacheKey {
    RedisCacheKey(RedisPool redisPool) {
        this.redisPool = redisPool;
    }

    @Override
    public Long expireSeconds(final Key key, final Long expire) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override public Long execute(Jedis jedis) throws CacheConnectionException {
                return jedis.expire(key.key(), expire);
            }
        }, key);
    }

    @Override public Long expireMillis(Key key, Long expireMills) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override public Long execute(Jedis jedis) throws CacheConnectionException {
                return jedis.pexpire(key.key(), expireMills);
            }
        }, key);
    }

    @Override public Long expireMillisAt(Key key, Long unixTimeMillis) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.pexpireAt(key.key(), unixTimeMillis);
            }
        }, key);
    }

    @Override
    public Long delete(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override public Long execute(Jedis jedis) throws CacheConnectionException {
                return jedis.del(key.key());
            }
        },key);
    }

    @Override
    public Long ttl(final Key key) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.ttl(key.key());
            }
        }, key);
    }

    @Override
    public Long expireSecondsAt(final Key key, final Long expire) throws CacheConnectionException {
        return redisPool.execute(new Executor<Long>() {
            @Override
            public Long execute(Jedis jedis) {
                return jedis.expireAt(key.key(), expire);
            }
        }, key);
    }
}
