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

import com.sparrow.cache.CacheMonitor;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.Pair;
import com.sparrow.protocol.constant.magic.Symbol;
import java.util.HashSet;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Named
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

    public void setConfig(JedisPoolConfig config) {
        this.config = config;
    }

    private JedisPoolConfig config;

    private JedisCluster jedisCluster;

    public CacheMonitor getCacheMonitor() {
        return cacheMonitor;
    }

    public String getInfo() {
        return config.toString();
    }

    public RedisPool() {
    }

    <T> T execute(Executor<T> executor, Key key) throws CacheConnectionException {
        try {
            Long startTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                if (!this.cacheMonitor.before(startTime, key)) {
                    return null;
                }
            }

            T result = executor.execute(jedisCluster);
            Long endTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                this.cacheMonitor.monitor(startTime, endTime, key);
            }
            return result;
        } catch (JedisConnectionException e) {
            logger.error(this.getInfo() + Symbol.COLON + e.getMessage());
            throw new CacheConnectionException(e.getMessage());
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        HashSet<HostAndPort> nodes = new HashSet<>();

        String[] urlArray = this.urls.split(Symbol.COMMA);

        //分割集群节点
        for (String url : urlArray) {
            Pair<String, String> urlPortPair = Pair.split(url, Symbol.COLON);
            nodes.add(new HostAndPort(urlPortPair.getFirst(), Integer.parseInt(urlPortPair.getSecond())));
        }

        //创建集群对象
        this.jedisCluster = new JedisCluster(nodes, 2000, config);
    }
}
