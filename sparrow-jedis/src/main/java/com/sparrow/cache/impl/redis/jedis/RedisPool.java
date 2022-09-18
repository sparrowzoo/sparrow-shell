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

import com.sparrow.cache.CacheMonitor;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.Pair;
import com.sparrow.protocol.constant.magic.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisPool implements ContainerAware {
    private Logger logger = LoggerFactory.getLogger(RedisPool.class);
    private CacheMonitor cacheMonitor;
    private String urls;

    public void setCacheMonitor(CacheMonitor cacheMonitor) {
        this.cacheMonitor = cacheMonitor;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    private JedisPoolConfig config;

    private JedisPool jedisPool;

    public CacheMonitor getCacheMonitor() {
        return cacheMonitor;
    }

    public RedisPool() {
    }

    <T> T execute(Executor<T> executor, Key key) throws CacheConnectionException {
        Jedis jedis = null;
        try {
            Long startTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                if (!this.cacheMonitor.before(startTime, key)) {
                    return null;
                }
            }

            jedis = this.jedisPool.getResource();
            T result = executor.execute(jedis);
            Long endTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                this.cacheMonitor.monitor(startTime, endTime, key);
            }
            return result;
        } catch (JedisConnectionException e) {
            logger.error(this.urls + Symbol.COLON + e.getMessage());
            throw new CacheConnectionException(e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        String[] urlArray = this.urls.split(Symbol.COMMA);
        //分割集群节点
        Pair<String, String> urlPortPair = Pair.split(urlArray[0], Symbol.COLON);
        this.jedisPool = new JedisPool(config, urlPortPair.getFirst(), Integer.parseInt(urlPortPair.getSecond()), 2000);
    }
}
