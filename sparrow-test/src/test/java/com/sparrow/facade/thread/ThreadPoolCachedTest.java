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

package com.sparrow.facade.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by harry on 2017/2/15.
 */
public class ThreadPoolCachedTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService fixedService = Executors.newCachedThreadPool();
        List<Future> futures = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Future future = fixedService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("runnable ...." + Thread.currentThread().getId());
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            futures.add(future);
        }
        fixedService.shutdown();

        for (Future future : futures) {
            future.cancel(true);
            System.out.println(future.isDone());
        }

        System.out.println("end....");
    }
}
