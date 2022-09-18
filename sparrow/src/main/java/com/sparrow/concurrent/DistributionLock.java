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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributionLock {
    private static Logger logger = LoggerFactory.getLogger(DistributionLock.class);

    private CacheClient cacheClient;
    private Key key;
    private static int expireMillis = 60 * 1000;

    private static final int MAX_SLEEP_MILLIS = 1024;

    private volatile boolean locked = false;

    public DistributionLock(CacheClient cacheClient, String lockKey) {

        this.cacheClient = cacheClient;
        this.key = Key.parse(lockKey);
    }

    public DistributionLock(CacheClient cacheClient, String lockKey, Integer expireSecs) {
        this(cacheClient, lockKey);
        this.expireMillis = expireSecs * 1000;
    }

    /**
     * @return lock key
     */
    public String getLockKey() {
        return this.key.key();
    }

    public long acquireLock() throws InterruptedException, CacheConnectionException {
        int times = 0;
        while (true) {
            //+1保证每次请求的expire 不同
            long expires = System.currentTimeMillis() + expireMillis + 1;
            if (cacheClient.string().setIfNotExist(key, expires + "")) { //同步的
                // lock acquired
                locked = true;
                return expires;
            }
            String currentExpireTimeMillis = cacheClient.string().get(key); //redis里的时间
            // lock is expired
            if (currentExpireTimeMillis != null && Long.parseLong(currentExpireTimeMillis) < System.currentTimeMillis()) {
                //multi thread
                /**
                 * e.g ..
                 * current time 1
                 * thread new_time old_time
                 * t1       2         1         equals 1
                 * t2       3         2         last_new=3 略大于2
                 */
                String oldExpire = cacheClient.string().getSet(key, expires);
                if (oldExpire != null && oldExpire.equals(currentExpireTimeMillis)) {
                    locked = true;
                    return expires;
                }
            }
            //加随机数线程相对公平
            int randomMillis = (int) (Math.random() * 10);
            int sleepTime = (1 << times++) + randomMillis;
            if (sleepTime >= MAX_SLEEP_MILLIS) {
                sleepTime = MAX_SLEEP_MILLIS + randomMillis;
            }
            Thread.sleep(sleepTime);
        }
    }

    public void release(long expire) throws CacheConnectionException {
        if (locked) {
            String oldExpire = cacheClient.string().get(key);
            if (oldExpire != null && Long.valueOf(oldExpire) == expire) {
                cacheClient.key().delete(key);
            }
            locked = false;
        }
    }
}
