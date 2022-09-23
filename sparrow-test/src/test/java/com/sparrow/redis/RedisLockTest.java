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

import com.sparrow.cache.Key;
import com.sparrow.concurrent.RedisLock;
import com.sparrow.constant.DateTime;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.ModuleSupport;
import com.sparrow.utility.DateTimeUtility;

public class RedisLockTest {
    public static void main(String[] args) throws InterruptedException {
        Container container = ApplicationContext.getContainer();
        ContainerBuilder builder=new ContainerBuilder();
        container.init(builder);
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
        Key.Business od = new Key.Business(lock, "lock");
        Key key = new Key.Builder().business(od).businessId(1000000).build();
        RedisLock redisLock = ApplicationContext.getContainer().getBean("redisLock");

//        boolean mainLock = redisLock.acquire(key, 10, 64);
//        System.out.println("主线程拿锁" + mainLock);
//        Thread.sleep(100);

        Runnable task = new Runnable() {
            @Override public void run() {
                while (true) {
                    try {
                        //System.out.println(Thread.currentThread().getName() + " getting lock");

                        boolean isLock = redisLock.acquire(key, 1000, 32);
                        if (isLock) {
                            Thread.sleep(1000);
                            System.err.println(Thread.currentThread().getName() + "-" + DateTimeUtility.getFormatCurrentTime(DateTime.FORMAT_YYYY_MM_DD_HH_MM_SS_MS));
                        } else {
                            //System.out.println(Thread.currentThread().getName() + " not got lock");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        redisLock.release(key);
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(task, "thread-" + i).start();
           // Thread.sleep(10);
        }
    }
}
