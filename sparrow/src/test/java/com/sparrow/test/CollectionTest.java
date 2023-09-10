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

package com.sparrow.test;

import com.sparrow.utility.CollectionsUtility;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class CollectionTest {

    public static void main(String[] args) throws InterruptedException {
        LinkedHashMap<Long, AtomicLong> linkedHashMap = new LinkedHashMap<Long, AtomicLong>();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        IntStream.range(0, 100).forEach(i -> new Thread(new Runnable() {
            @Override
            public void run() {
                for (long j = 0; j < 100000; j++) {
                    synchronized (linkedHashMap) {
                        CollectionsUtility.incrementByKey(linkedHashMap, j, 1000);
                    }
                }
                countDownLatch.countDown();
            }
        }).start());
        countDownLatch.await();
        System.out.println(linkedHashMap);
    }
}
