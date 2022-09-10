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

package com.sparrow.cache;

import com.sparrow.concurrent.AbstractLock;
import com.sparrow.constant.cache.KEY;
import com.sparrow.exception.CacheConnectionException;
import com.sparrow.utility.StringUtility;
import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RedisLock extends AbstractLock {
    @Inject
    private CacheClient cacheClient;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 1. 不能死锁
     * <p>
     * 2. 不能把老锁删掉
     * <p>
     * 3. 并发问题
     *
     * @param key
     * @param expireSeconds
     * @return
     */
    @Override
    protected Boolean tryLock(KEY key, int expireSeconds) {
        //系统时间+设置的过期时间
        String expiresValue = this.getUnique() + "";
        try {
            if (cacheClient.string().setIfNotExist(key, expiresValue)) {
                //cacheClient.key().expire(key, expireSeconds);
                // 如果当前锁不存在，返回加锁成功
                logger.info("first got lock successful");
                return true;
            }
            if (!lock.tryLock()) {
                return false;
            }
            // 如果锁已经存在，获取锁的过期时间
            String currentLockValue = cacheClient.string().get(key);
            // 如果获取到的过期时间，小于系统当前时间，表示已经过期
            if (currentLockValue != null) {
                long currentLockExpires = Long.parseLong(currentLockValue);
                long diffSeconds = Duration.ofNanos(System.nanoTime() - currentLockExpires).getSeconds();
                if (diffSeconds > expireSeconds) {
                    // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
                    //todo getSet 不是原子的？ 没拿到锁，但是内容已经改变，线程ID已经变了
                    //todo watch cas
                    String oldLockValue = cacheClient.string().getSet(key, expiresValue);
                    // 考虑多进程(当前进程不可能)并发的情况，只有一个线程的设置值和当前值相同，它才可以加锁
                    if (oldLockValue != null && oldLockValue.equals(currentLockValue)) {
                        cacheClient.key().expire(key, expireSeconds);
                        logger.info("not expire current:{}-redis:{},duration {}",
                            expiresValue,
                            currentLockValue,
                            diffSeconds);
                        return true;
                    }
                    return false;
                } else {
                    logger.info("current:{}-redis:{},duration {}",
                        expiresValue,
                        currentLockValue,
                        diffSeconds);
                }
                //todo 这段代码是不是有并发问题
                if (getUnique().equals(currentLockExpires)) {
                    //reentrant lock
                    logger.info("current:{}-redis:{} reentrant-lock", expiresValue, currentLockValue);
                    return true;
                }
            }
            //其他情况，均返回加锁失败
            return false;
        } catch (CacheConnectionException e) {
            //链接失败 加锁失败
            logger.error("{} get connection fail", key.key(), e);
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public Boolean release(KEY key) {
        try {
            String value = cacheClient.string().get(key);
            if (StringUtility.isNullOrEmpty(value)) {
                return false;
            }

            //todo 有数组越界的问题
            if (Long.valueOf(value).equals(this.getUnique())) {
                return cacheClient.key().delete(key);
            }
            return false;
        } catch (CacheConnectionException e) {
            return false;
        }
    }
}