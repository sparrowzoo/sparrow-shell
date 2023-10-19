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

import com.sparrow.utility.StringUtility;

public abstract class RedisLock extends AbstractLock {
    public Long setGetMillisTimeUnique() {
        Long unique = System.currentTimeMillis();
        this.uniques.set(unique);
        return unique;
    }

    protected abstract boolean setIfNotExist(String key, Long currentMills);

    protected abstract void expireMillis(String key, Long expireMillis);

    protected abstract String getSet(String key, Long currentMills);

    protected abstract long ttl(String key);

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
    protected Boolean tryAcquire(String key, long expireMillis) {
        //系统时间+设置的过期时间
        Long currentMills = this.setGetMillisTimeUnique();
        //https://www.cnblogs.com/somefuture/p/13690961.html
        //毫秒和纳秒使用场景不同
        //long expireAt = System.currentTimeMillis() + expireMillis;
        try {
            //setnx and expire 非原子操作
            if (this.setIfNotExist(key, currentMills)) {
                /**
                 * setnx 和 expire 不是原子性的操作，假设某个线程执行setnx 命令，
                 * 成功获得了锁，但是还没来得及执行expire 命令，服务器就挂掉了，
                 * 这样一来，这把锁就没有设置过期时间了，变成了死锁，
                 * 别的线程再也没有办法获得锁了。
                 */
                this.expireMillis(key, expireMillis);
                // 如果当前锁不存在，返回加锁成功
                logger.error("first got lock successful");
                return true;
            }

            // 如果锁已经存在，获取锁的过期时间
            String lockCreatedUnique = this.get(key);
            // 特别的，当已存在的锁currentLockValue为空时，应该重新SETNX
            if (lockCreatedUnique == null) {
                if (this.setIfNotExist(key, currentMills)) {
                    this.expireMillis(key, expireMillis);
                    // 如果当前锁不存在，返回加锁成功
                    logger.info("first got lock successful");
                    return true;
                }
            }
            // 如果获取到的过期时间，小于系统当前时间，表示已经过期
            //currentLockValue != null
            else {
                long lockCreatedMillis = Long.parseLong(lockCreatedUnique);
                //set ex 未设置的情况
                if (this.isExpire(lockCreatedMillis, expireMillis)) {
                    Long ttl = this.ttl(key);
                    logger.error("ttl {}", ttl);
                    // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
                    //todo getSet 不是原子的？ 没拿到锁，但是内容已经改变，线程ID已经变了
                    /**
                     * 如果前一个锁超时的时候，刚好有多台服务器去请求获取锁，
                     * 那么就会出现同时执行redis.getset()而导致出现过期时间覆盖问题，
                     * 不过这种情况并不会对正确结果造成影响
                     */
                    String oldLockUnique = this.getSet(key, currentMills);
                    if (oldLockUnique == null) {
                        this.expireMillis(key, expireMillis);
                        return true;
                    }

                    // 考虑线程并发的情况，只有一个线程的设置值和当前值相同，它才可以加锁
                    if (oldLockUnique.equals(lockCreatedUnique)) {
                        this.expireMillis(key, expireMillis);
                        logger.error("getset got lock not expire current:{}-redis:{}",
                                currentMills,
                                lockCreatedUnique);
                        return true;
                    } else {
                        logger.error("lock fail but set expire current:{}-redis:{}",
                                currentMills,
                                lockCreatedUnique);
                    }
                    return false;
                }
                //未过期
                if (this.getMillisTimeUnique() == lockCreatedMillis) {
                    //reentrant lock
                    logger.info("current:{}-redis:{} reentrant-lock", currentMills, lockCreatedUnique);
                    return true;
                }
            }
            //其他情况，均返回加锁失败
            return false;
        } catch (Exception e) {
            //链接失败 加锁失败
            logger.error("{} get connection fail", key, e);
            return false;
        }
    }

    public Boolean release(String key) {
        try {
            String value = this.get(key);
            if (StringUtility.isNullOrEmpty(value)) {
                return false;
            }
            //可能由于锁覆盖导致无法删除
            //V2.0 不存在该情况
            if (Long.parseLong(value) == this.getMillisTimeUnique()) {
                return this.delete(key);
            }
            return false;
        } finally {
            this.removeUnique();
        }
    }
}
