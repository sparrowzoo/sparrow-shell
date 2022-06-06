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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by harry on 2017/2/15.
 */
public class ThreadPoolScheduleTest {
    public static void main(String[] args) {
        ScheduledExecutorService fixedService = Executors.newScheduledThreadPool(1);
        ScheduledFuture scheduledFuture = fixedService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.print("current" + Thread.currentThread().getId());
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);


        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduledFuture.cancel(false);
        /// for (;;) {
        fixedService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("submit" + Thread.currentThread().getId());
            }
        }, 0, TimeUnit.SECONDS);
        // }
    }
}
