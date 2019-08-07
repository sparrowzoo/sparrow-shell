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

package com.sparrow.cache.impl.redis.lettuce;

import com.sparrow.cache.CacheMonitor;
import com.sparrow.constant.cache.KEY;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.exception.CacheConnectionException;
import com.sparrow.protocol.constant.magic.SYMBOL;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author harry
 */
public class RedisPool implements ContainerAware {
    private Logger logger = LoggerFactory.getLogger(RedisPool.class);
    static final RedisCodec<byte[], byte[]> CODEC = ByteArrayCodec.INSTANCE;
    private GenericObjectPoolConfig config;
    private GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool;
    private CacheMonitor cacheMonitor;
    private String urls;

    public void setUrls(String urls) {
        this.urls = urls;
    }

    private RedisClusterClient clusterClient = null;

    public void setConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.config = genericObjectPoolConfig;
    }

    public void setPool(GenericObjectPool<StatefulRedisClusterConnection<byte[], byte[]>> pool) {
        this.pool = pool;
    }

    public void setCacheMonitor(CacheMonitor cacheMonitor) {
        this.cacheMonitor = cacheMonitor;
    }

    public String getInfo() {
        return this.config.toString();
    }

    public RedisPool() {
    }

    <T> T execute(Executor<T> executor, KEY key) throws CacheConnectionException {

        try {
            StatefulRedisClusterConnection<byte[], byte[]> connection = pool.borrowObject();
            Long startTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                if (!this.cacheMonitor.before(startTime, key)) {
                    return null;
                }
            }
            RedisAdvancedClusterCommands<byte[], byte[]> sync = connection.sync();
            T result = executor.execute(sync);
            Long endTime = System.currentTimeMillis();
            if (this.cacheMonitor != null) {
                this.cacheMonitor.monitor(startTime, endTime, key);
            }
            this.pool.returnObject(connection);
            return result;
        } catch (Exception e) {
            logger.error(this.getInfo() + SYMBOL.COLON + e.getMessage());
            throw new CacheConnectionException(e.getMessage());
        }
    }

    @Override
    public void aware(Container container, String beanName) {
        String[] urlArray = this.urls.split(SYMBOL.COMMA);
        List<RedisURI> redisUrls = new ArrayList<>(urlArray.length);
        for (String url : urlArray) {
            redisUrls.add(RedisURI.create(url));
        }
        this.clusterClient = RedisClusterClient.create(redisUrls);
        pool = ConnectionPoolSupport.createGenericObjectPool(() -> clusterClient.connect(CODEC), this.config);
    }
}
