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

import com.sparrow.utility.DateTimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLock {
    protected static Logger logger = LoggerFactory.getLogger(AbstractLock.class);

    /**
     * 毫秒级唯一key，因为分布式锁，同一时刻只能有一个线程拿到锁
     * 所以毫秒级时间戮可以唯一标识一个锁
     * 为支持1.0 版本，由于setnx exp非原子操作，导致exp 可能失效.
     */
    protected ThreadLocal<Long> uniques = new ThreadLocal<>();
    public static final String SUCCESS = "SUCCESS";
    public static final String RETRY_TIMEOUT = "RETRY_TIMEOUT";
    public static final String FAIL = "FAIL";

    protected abstract boolean delete(String key);

    protected abstract String get(String key);

    protected Long getMillisTimeUnique() {
        return this.uniques.get();
    }

    protected Long setGetMillisTimeUnique() {
        Long unique = System.currentTimeMillis();
        this.uniques.set(unique);
        return unique;
    }

    protected void monitor(String key, String status) {
        logger.info("LOCK-MONITOR KEY={},STATUS={}", key, status);
    }

    protected boolean isExpire(long lockTime, long expireMillis) {
        long diffMills = (System.currentTimeMillis() - lockTime);
        return diffMills > expireMillis;
    }

    protected void removeUnique() {
        this.uniques.remove();
    }

    protected abstract Boolean tryAcquire(String key, long expireMillis);

    public abstract Boolean release(String key);

    public boolean acquire(String key, int expireMillis) {
        return this.acquire(key, expireMillis, 0);
    }

    public boolean acquire(String key, int expireMillis, int retryMillis) {
        Boolean lock = this.tryAcquire(key, expireMillis);
        if (retryMillis <= 0) {
            this.monitor(key, lock ? SUCCESS : FAIL);
            return lock;
        }

        int times = 1;
        int timeout = 0;
        long t = this.getMillisTimeUnique();
        while (lock == null || !lock) {
            lock = this.tryAcquire(key, expireMillis);
            if (lock) {
                logger.info("retry times {} got lock duration {}", times, (System.nanoTime() - t) / 1000000);
                this.monitor(key, SUCCESS);
                return true;
            }
            try {
                if (timeout < 1024) {
                    timeout += 1 << times++;
                }
                if (System.currentTimeMillis() - t > retryMillis) {
                    this.monitor(key, RETRY_TIMEOUT);
                    return false;
                }
                Thread.sleep(timeout);
                logger.debug("lock {} timeout {} at [{}] {}", key, timeout, DateTimeUtility.getFormatCurrentTime(), System.currentTimeMillis());
            } catch (InterruptedException ignore) {
                this.release(key);
            }
        }
        return true;
    }
}
