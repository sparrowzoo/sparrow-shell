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

package com.sparrow.redis;

import com.sparrow.cache.CacheClient;
import com.sparrow.cache.RedisLock;
import com.sparrow.constant.cache.KEY;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.ModuleSupport;
import java.util.Random;

public class RedisLockTest {
    public static void main(String[] args) throws InterruptedException {
        Container container = ApplicationContext.getContainer();
        container.setContextConfigLocation("/sparrow_application_context.xml");
        container.init();
        //定义模块，一个业务会存在多个模块
        ModuleSupport lock = new ModuleSupport() {
            @Override
            public String code() {
                return "01";
            }

            @Override
            public String name() {
                return "goods";
            }
        };
        //相同模块下会存在多个业务
        KEY.Business od = new KEY.Business(lock, "lock");
        KEY key = new KEY.Builder().business(od).businessId(new Random().nextInt(2000)).build();
        RedisLock redisLock = ApplicationContext.getContainer().getBean("redisLock");

        boolean mainLock = redisLock.retryAcquireLock(key, 1, 1000);

        System.out.println("主线程拿锁"+mainLock);
        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override public void run() {
                //todo 这里不对
                boolean childLock = redisLock.retryAcquireLock(key, 1, 1000);
                System.out.println("子线程拿锁"+childLock);
                childLock = redisLock.retryAcquireLock(key, 1, 1000);
                System.out.println("子线程重入锁"+childLock);
                childLock = redisLock.retryAcquireLock(key, 1, 1000);
                System.out.println("子线程重入锁"+childLock);

                if (!childLock) {
                    return;
                }
                System.out.println(childLock);
            }
        }).start();
        redisLock.release(key);
        System.out.println(mainLock);
    }
}
