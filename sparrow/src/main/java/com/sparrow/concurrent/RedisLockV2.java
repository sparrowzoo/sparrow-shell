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

package com.sparrow.concurrent;

import com.sparrow.cache.CacheClient;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.utility.StringUtility;
import java.util.concurrent.locks.ReentrantLock;

public class RedisLockV2 extends AbstractLock {
    private CacheClient cacheClient;

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    private ReentrantLock localLock = new ReentrantLock();

    /**
     * 1. 不能死锁
     * <p>
     * 2. 不能把老锁删掉
     * <p>
     * 3. 并发问题
     *
     * @param key
     * @param expireMillis
     * @return
     */
    @Override
    protected Boolean tryAcquire(Key key, long expireMillis) {
        //系统时间+设置的过期时间
        long unique = this.setGetUnique();
        String expiresValue = unique + "";
        //https://www.cnblogs.com/somefuture/p/13690961.html
        //毫秒和纳秒使用场景不同
        try {
            if (!localLock.tryLock()) {
                return false;
            }
            if (cacheClient.string().setIfNotExistWithMills(key, expiresValue,expireMillis)) {
                // 如果当前锁不存在，返回加锁成功
                logger.error("first got lock successful");
                return true;
            }
            //其他情况，均返回加锁失败
            return false;
        } catch (CacheConnectionException e) {
            //链接失败 加锁失败
            logger.error("{} get connection fail", key.key(), e);
            return false;
        } finally {
            if (localLock.isHeldByCurrentThread()) {
                localLock.unlock();
            }
        }
    }

    public Boolean release(Key key) {
        try {
            String value = cacheClient.string().get(key);
            if (StringUtility.isNullOrEmpty(value)) {
                return false;
            }
            //todo 保证原子性
            if (Long.valueOf(value).equals(this.getUnique())) {
                return cacheClient.key().delete(key) > 0;
            }
            return false;
        } catch (CacheConnectionException e) {
            return false;
        } finally {
            this.removeUnique();
        }
    }
}