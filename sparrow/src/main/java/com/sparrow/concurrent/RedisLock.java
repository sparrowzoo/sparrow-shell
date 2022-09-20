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

public class RedisLock extends AbstractLock {
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
        //long expireAt = System.currentTimeMillis() + expireMillis;
        try {
            if (!localLock.tryLock()) {
                return false;
            }
            if (cacheClient.string().setIfNotExist(key, expiresValue)) {
                //过期时间
                cacheClient.key().expireMillis(key,expireMillis);
                // 如果当前锁不存在，返回加锁成功
                logger.error("first got lock successful");
                return true;
            }

            // 如果锁已经存在，获取锁的过期时间
            String currentLockValue = cacheClient.string().get(key);
            // 特别的，当已存在的锁currentLockValue为空时，应该重新SETNX
            if (currentLockValue == null) {
                if (cacheClient.string().setIfNotExist(key, expiresValue)) {
                    cacheClient.key().expireMillis(key, expireMillis);
                    // 如果当前锁不存在，返回加锁成功
                    logger.info("first got lock successful");
                    return true;
                }
            }
            // 如果获取到的过期时间，小于系统当前时间，表示已经过期
            if (currentLockValue != null) {
                long currentLockExpires = Long.parseLong(currentLockValue);
                if (this.isExpire(currentLockExpires, expireMillis)) {
                    Long ttl = cacheClient.key().ttl(key);
                    logger.error("ttl {}", ttl);

                    // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
                    //todo getSet 不是原子的？ 没拿到锁，但是内容已经改变，线程ID已经变了
                    String oldLockValue = cacheClient.string().getSet(key, expiresValue);
                    if (oldLockValue == null) {
                        cacheClient.key().expireMillis(key, expireMillis);
                        return true;
                    }
                    // 考虑多进程(当前进程不可能)并发的情况，只有一个线程的设置值和当前值相同，它才可以加锁
                    if (oldLockValue.equals(currentLockValue)) {
                        cacheClient.key().expireMillis(key, expireMillis);
                        logger.error("getset got lock not expire current:{}-redis:{}",
                            expiresValue,
                            currentLockValue);
                        return true;
                    } else {
                        logger.error("lock fail but set expire current:{}-redis:{}",
                            expiresValue,
                            currentLockValue);
                    }
                    return false;
                } else {
                    //未过期
                    if (this.getUnique() == currentLockExpires) {
                        //reentrant lock
                        logger.info("current:{}-redis:{} reentrant-lock", expiresValue, currentLockValue);
                        return true;
                    }
                }
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