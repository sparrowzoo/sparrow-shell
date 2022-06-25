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
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract Boolean readLock(String key);

    public boolean retryAcquireLock(String key) {
        Boolean lock = this.readLock(key);
        int times = 1;
        int timeout = 0;
        while (lock == null || !lock) {
            lock = this.readLock(key);
            try {
                if (timeout < 1024) {
                    timeout = 1 << times++;
                }
                Thread.sleep(timeout);
                logger.debug("lock {} timeout {} at [{}] {}", key, timeout, DateTimeUtility.getFormatCurrentTime(), System.currentTimeMillis());
            } catch (InterruptedException ignore) {
            }
        }
        return true;
    }
}
