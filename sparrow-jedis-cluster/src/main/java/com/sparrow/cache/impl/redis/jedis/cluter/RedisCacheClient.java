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

package com.sparrow.cache.impl.redis.jedis.cluter;

import com.sparrow.cache.CacheClient;
import com.sparrow.cache.CacheHash;
import com.sparrow.cache.CacheKey;
import com.sparrow.cache.CacheList;
import com.sparrow.cache.CacheSet;
import com.sparrow.cache.CacheSortedSet;
import com.sparrow.cache.CacheString;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import javax.inject.Inject;
import javax.inject.Named;

@Named("cacheClient")
public class RedisCacheClient implements CacheClient, ContainerAware {
    private CacheString cacheString;
    private CacheSet cacheSet;
    private CacheSortedSet cacheSortedSet;
    private CacheHash cacheHash;
    private CacheKey cacheKey;
    private CacheList cacheList;

    @Inject
    private RedisPool redisPool;

    @Override
    public CacheString string() {
        return cacheString;
    }

    @Override
    public CacheSet set() {
        return cacheSet;
    }

    @Override
    public CacheSortedSet sortedSet() {
        return cacheSortedSet;
    }

    @Override
    public CacheHash hash() {
        return cacheHash;
    }

    @Override
    public CacheKey key() {
        return cacheKey;
    }

    @Override
    public CacheList list() {
        return cacheList;
    }

    @Override public void aware(Container container, String beanName) {
        this.cacheKey = new RedisCacheKey(redisPool);
        this.cacheString = new RedisCacheString(redisPool);
        this.cacheSet = new RedisCacheSet(redisPool);
        this.cacheSortedSet = new RedisCacheSortedSet(redisPool);
        this.cacheHash = new RedisCacheHash(redisPool);
        this.cacheList = new RedisCacheList(redisPool);
    }
}
