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

public abstract class RedisLockV2 extends AbstractLock {
    @Override
    protected boolean delete(String key) {
        return false;
    }

    @Override
    protected String get(String key) {
        return null;
    }

    protected abstract boolean setIfNotExistWithMills(String key, Long unique, long expireMillis);

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
        long unique = this.setGetMillisTimeUnique();
        //https://www.cnblogs.com/somefuture/p/13690961.html
        //毫秒和纳秒使用场景不同
        try {
            /**
             * SetParams setParams = new SetParams();
             * setParams.nx().px(expireMills);
             * return "OK".equalsIgnoreCase(jedis.set(key.key(), v, setParams));
             */
            if (this.setIfNotExistWithMills(key, unique, expireMillis)) {
                // 如果当前锁不存在，返回加锁成功
                logger.error("first got lock successful");
                return true;
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
            //todo 保证原子性
            if (Long.parseLong(value) == this.getMillisTimeUnique()) {
                return this.delete(key);
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            this.removeUnique();
        }
    }
}
