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

import com.sparrow.constant.cache.KEY;
import com.sparrow.support.IpSupport;
import com.sparrow.utility.DateTimeUtility;
import java.time.Duration;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLock {
    protected static Logger logger = LoggerFactory.getLogger(AbstractLock.class);

    private ThreadLocal<Long> uniques = new ThreadLocal<>();

    protected Long getUnique() {
        Long t = this.uniques.get();
        if (t != null) {
            logger.info("old unique {}", t);
            return t;
        }
        t = System.nanoTime();
        this.uniques.set(t);
        logger.info("new unique {}", t);
        return t;
    }

    protected abstract Boolean tryLock(KEY key, int expireSeconds);

    public abstract Boolean release(KEY key);

    public boolean retryAcquireLock(KEY key, int expireSeconds, int retryMillis) {
        long unique = this.getUnique();
        Boolean lock = this.tryLock(key, expireSeconds);
        //纳秒级，并作为唯一标识，因为用ip+线程ID作为唯一标识在锁释放时可能会导致无法正常删除
        //同时防止毫秒级拿到多个锁

        int times = 1;
        int timeout = 0;
        //todo 为什么要重试？直接跳出返回行不行？
        while (lock == null || !lock) {
            lock = this.tryLock(key, expireSeconds);
            if(lock){
                return true;
            }
            try {
                if (timeout < 1024) {
                    timeout += 1 << times++;
                }
                if (System.nanoTime() - unique > retryMillis*1000000) {
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