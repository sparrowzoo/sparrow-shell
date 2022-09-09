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
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class RedisLock extends AbstractLock {
    @Inject
    private CacheClient cacheClient;

    /**
     * 1. 不能死锁
     * 2. 不能把老锁删掉
     * 3. 并发问题
     * @param key
     * @param expireSeconds
     * @param uniqueKey
     * @return
     */
    @Override
    protected Boolean tryLock(KEY key, int expireSeconds, String uniqueKey) {
        //系统时间+设置的过期时间
        String expiresValue = System.currentTimeMillis() + expireSeconds * 1000 + "|" + uniqueKey;
        try {
            if (cacheClient.string().setIfNotExist(key, expiresValue)) {
                cacheClient.key().expire(key,expireSeconds);
                // 如果当前锁不存在，返回加锁成功
                return true;
            }
            // 如果锁已经存在，获取锁的过期时间
            String currentLockValue = cacheClient.string().get(key);
            // 如果获取到的过期时间，小于系统当前时间，表示已经过期
            if (currentLockValue != null) {
                logger.info("{}-{}",uniqueKey,currentLockValue);
                //todo 直接操作数组不优雅？ 有索引越界的风险
                String[] lockValueArray = currentLockValue.split("\\|");
                String currentUnique = this.getUniqueKey();

                long currentLockExpires = Long.parseLong(lockValueArray[0]);
                if (currentLockExpires < System.currentTimeMillis()) {
                    // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
                    //todo getSet 不是原子的？ 没拿到锁，但是内容已经改变，线程ID已经变了
                    //todo watch cas
                    String oldLockValue = cacheClient.string().getSet(key, expiresValue);
                    // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才可以加锁
                    if(oldLockValue != null && oldLockValue.equals(currentLockValue)){
                        cacheClient.key().expire(key,expireSeconds);
                        return true;
                    }
                    return false;
                }
                //todo 这段代码是不是有并发问题
                if (currentUnique.equals(lockValueArray[1])) {
                    //reentrant lock
                    return true;
                }
            }
            //其他情况，均返回加锁失败
            return false;
        } catch (CacheConnectionException e) {
            //链接失败 加锁失败
            logger.error("{} get connection fail", key.key(), e);
            return false;
        }
    }

    public Boolean release(KEY key) {
        try {
            String value = cacheClient.string().get(key);
            if (StringUtility.isNullOrEmpty(value)) {
                return false;
            }
            String[] valueArray = value.split("\\|");
            //todo 有数组越界的问题
            if (valueArray[1].equals(this.getUniqueKey())) {
                return cacheClient.key().delete(key);
            }
            return false;
        } catch (CacheConnectionException e) {
            return false;
        }
    }
}