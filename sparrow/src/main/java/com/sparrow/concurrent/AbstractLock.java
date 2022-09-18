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

import com.sparrow.cache.Key;
import com.sparrow.utility.DateTimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLock {
    protected static Logger logger = LoggerFactory.getLogger(AbstractLock.class);

    private ThreadLocal<Long> uniques = new ThreadLocal<>();

    public static final String SUCCESS = "SUCCESS";
    public static final String RETRY_TIMEOUT = "RETRY_TIMEOUT";
    public static final String FAIL = "FAIL";

    protected long getUnique() {
        return this.uniques.get();
    }

    protected long setGetUnique() {
        long nanoTime = System.nanoTime();
        this.uniques.set(nanoTime);
        return nanoTime;
    }

    protected void monitor(Key key, String status) {
        logger.info("LOCK-MONITOR KEY={},STATUS={}", key.key(), status);
    }

    protected boolean isExpire(long lockTime, long expireMillis) {
        long diffMills = (System.nanoTime() - lockTime) / 1000000;
        return diffMills > expireMillis;
    }

    protected void removeUnique() {
        this.uniques.remove();
    }

    protected abstract Boolean tryAcquire(Key key, long expireMillis);

    public abstract Boolean release(Key key);

    public boolean acquire(Key key, int expireMillis, int retryMillis) {
        Boolean lock = this.tryAcquire(key, expireMillis);
        if (retryMillis <= 0) {
            this.monitor(key, lock ? SUCCESS : FAIL);
            return lock;
        }
        //纳秒级，并作为唯一标识，因为用ip+线程ID作为唯一标识在锁释放时可能会导致无法正常删除
        //同时防止毫秒级拿到多个锁

        int times = 1;
        int timeout = 0;
        long t = this.getUnique();
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
                if (System.nanoTime() - t > retryMillis * 1000000L) {
                    this.monitor(key, RETRY_TIMEOUT);
                    return false;
                }
                Thread.sleep(timeout);
                logger.debug("lock {} timeout {} at [{}] {}", key, timeout, DateTimeUtility.getFormatCurrentTime(), System.currentTimeMillis());
            } catch (InterruptedException ignore) {
                //todo 线程被中断怎么处理比较好
                this.release(key);
            }
        }
        return true;
    }
}