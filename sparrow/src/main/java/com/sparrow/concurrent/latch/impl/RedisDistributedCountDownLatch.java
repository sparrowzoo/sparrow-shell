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

package com.sparrow.concurrent.latch.impl;

import com.sparrow.cache.CacheClient;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.concurrent.latch.DistributedCountDownLatch;
import com.sparrow.utility.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisDistributedCountDownLatch implements DistributedCountDownLatch {
    private static Logger logger = LoggerFactory.getLogger(RedisDistributedCountDownLatch.class);
    private CacheClient cacheClient;

    public RedisDistributedCountDownLatch() {
    }

    public RedisDistributedCountDownLatch(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    @Override
    public void consume(Key monitoryKey, final String key) {
        if (monitoryKey == null) {
            throw new UnsupportedOperationException("monitor key is null");
        }
        while (true) {
            try {
                cacheClient.string().increase(monitoryKey, -1L);
                break;
            } catch (CacheConnectionException e) {
                logger.error("monitor consume connection break ", e);
            }
        }

    }

    @Override
    public void product(Key productKey, final String key) {
        if (productKey == null) {
            throw new UnsupportedOperationException("product key is null");
        }
        while (true) {
            try {
                cacheClient.string().increase(productKey, 1L);
                return;
            } catch (CacheConnectionException e) {
                logger.error("productKey product connection break ", e);
            }
        }
    }

    @Override
    public boolean isFinish(Key monitorKey) {
        if (StringUtility.isNullOrEmpty(monitorKey)) {
            throw new UnsupportedOperationException("product key is null");
        }
        try {
            Long productCount = cacheClient.string().get(monitorKey, Long.class);
            //must be equal 0
            Boolean match = productCount == 0L;
            logger.info("product key {}:count {},match {}", monitorKey.key(),
                productCount,
                match);
            if (!match) {
                return false;
            }
        } catch (CacheConnectionException e) {
            return false;
        }
        while (true) {
            try {
                cacheClient.key().delete(monitorKey);
                return true;
            } catch (CacheConnectionException ignore) {
                logger.error("monitor error", ignore);
            }
        }
    }

    @Override
    public boolean monitor(Key monitorKey, int secondInterval) {
        while (true) {
            if (isFinish(monitorKey)) {
                return true;
            }
            try {
                Thread.sleep(1000 * secondInterval);
            } catch (InterruptedException ignore) {
            }
        }
    }

    @Override
    public boolean monitor(Key monitorKey) {
        return this.monitor(monitorKey, 2);
    }
}
