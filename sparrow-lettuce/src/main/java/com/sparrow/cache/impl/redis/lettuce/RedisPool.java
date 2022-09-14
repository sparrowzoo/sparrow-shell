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

import com.sparrow.cache.CacheMonitor;
import com.sparrow.constant.cache.KEY;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.Pair;
import com.sparrow.exception.CacheConnectionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/**
 * @author harry
 */
public class RedisPool implements ContainerAware {
    private Logger logger = LoggerFactory.getLogger(RedisPool.class);
    private ShardedJedisPool pool = null;
    private CacheMonitor cacheMonitor;

    private String urls;
    private Integer maxActive = 100;
    private Integer maxIdle = 50;
    private Integer maxWait = 50000;
    private Boolean testOnBorrow = true;

    public void setCacheMonitor(CacheMonitor cacheMonitor) {
        this.cacheMonitor = cacheMonitor;
    }

    public CacheMonitor getCacheMonitor() {
        return cacheMonitor;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getInfo() {
        StringBuilder poolInfo = new StringBuilder();
        poolInfo.append(",url:");
        poolInfo.append(urls);
        poolInfo.append("maxActive:");
        poolInfo.append(maxActive);
        poolInfo.append("maxIdle:");
        poolInfo.append(this.maxIdle);
        poolInfo.append("maxWait:");
        poolInfo.append(this.maxWait);//超时时间
        poolInfo.append("testOnBorrow");
        poolInfo.append(this.testOnBorrow);
        return poolInfo.toString();
    }

    public RedisPool() {
    }

    <T> T execute(Executor<T> executor, KEY key) throws CacheConnectionException {
        ShardedJedis jedis = null;
        try {
            Long startTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                if (!this.cacheMonitor.before(startTime, key)) {
                    return null;
                }
            }

            jedis = this.pool.getResource();
            T result = executor.execute(jedis);
            this.pool.returnResource(jedis);
            Long endTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                this.cacheMonitor.monitor(startTime, endTime, key);
            }
            this.pool.returnResource(jedis);
            return result;
        } catch (JedisConnectionException e) {
            this.pool.returnBrokenResource(jedis);
            logger.error(this.getInfo() + SYMBOL.COLON + e.getMessage());
            throw new CacheConnectionException(e.getMessage());
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        JedisPoolConfig config = new JedisPoolConfig();
        // 最大活动链接
        config.setMaxActive(this.maxActive);
        config.setMaxIdle(this.maxIdle);
        config.setMaxWait(this.maxWait);//超时时间
        config.setTestOnBorrow(this.testOnBorrow);

        // 超过时则报错 阻塞 或增加链接数
        config.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_FAIL);
        String[] urlArray = this.urls.split(SYMBOL.COMMA);
        List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(urlArray.length);
        for (String url : urlArray) {
            Pair<String, String> urlPortPair = Pair.split(url, SYMBOL.COLON);
            JedisShardInfo infoA = new JedisShardInfo(urlPortPair.getFirst(), urlPortPair.getSecond());
            jdsInfoList.add(infoA);
        }
        pool = new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH,
            Sharded.DEFAULT_KEY_TAG_PATTERN);
    }
}
