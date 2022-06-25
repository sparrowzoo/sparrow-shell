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
package com.sparrow.core.algorithm.bus;

import com.sparrow.concurrent.SparrowThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BatchEventBus<T> {
    private Logger logger = LoggerFactory.getLogger(BatchEventBus.class);

    private ArrayBlockingQueue<T> queue;

    private ThreadPoolExecutor consumerThreadPool;

    private int batchCount;

    private long lastPersistTime;

    private List<T> batchList;

    private int batchSize;

    private OverflowProcessor overflowProcessor;

    private boolean flush = false;

    private BatchEventBus(BatchEventBus.Builder builder) {

        this.queue = new ArrayBlockingQueue<>(builder.capacity, true);

        this.batchSize = builder.batchSize;

        this.batchList = new ArrayList<>(builder.batchSize);

        this.overflowProcessor = builder.overflowProcessor;

        this.consumerThreadPool = new ThreadPoolExecutor(builder.threadPoolCoreSize, builder.threadPoolMaxSize,
            5L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(builder.threadPoolCapacity),
            new SparrowThreadFactory.Builder().namingPattern("batch-event-consumer-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());

        ExecutorService dispatcherThread = Executors.newSingleThreadExecutor(new SparrowThreadFactory.Builder().namingPattern("batch-event-dispatcher-%d").build());
        dispatcherThread.submit(new Runnable() {
            @Override
            public void run() {
                final BatchEventBus batchEventBus = BatchEventBus.this;
                lastPersistTime = System.currentTimeMillis();
                while (true) {
                    try {
                        List<T> drainList = new ArrayList<>(batchSize);
                        batchEventBus.queue.drainTo(drainList, batchSize);
                        batchList.addAll(drainList);
                        //batch size must be >0
                        //1. batch size
                        //2. timeout
                        //3. flush manually
                        if (batchList.size() > 0 && (batchList.size() >= batchSize || (System.currentTimeMillis() - batchEventBus.lastPersistTime) / 1000 > builder.batchSecondTimeout || flush)) {
                            List<T> submittingBatch = new ArrayList<>(batchList);

                            BatchEventBus.this.consumerThreadPool.submit(new Runnable() {
                                @Override
                                public void run() {
                                    batchEventBus.overflowProcessor.hook(submittingBatch);
                                    batchEventBus.lastPersistTime = System.currentTimeMillis();
                                    batchEventBus.flush = false;
                                    batchEventBus.batchCount++;
                                }
                            });
                            batchList.clear();
                        }
                    } catch (Throwable e) {
                        logger.error("batch-event-dispatcher-error", e);
                    }
                }
            }
        });
    }

    public void publish(T item) throws InterruptedException {
        if (item == null) {
            return;
        }
        queue.put(item);
    }

    public void flush() {
        this.flush = true;
    }

    public ArrayBlockingQueue<T> getQueue() {
        return queue;
    }

    public boolean isEmpty() {
        return this.queue.size() == 0;
    }

    public Integer getBatchCount() {
        return batchCount + (this.queue.size() > 0 ? 1 : 0);
    }

    public static class Builder {
        Integer capacity = 10000000;

        Integer batchSize = 10000;

        OverflowProcessor overflowProcessor;

        int batchSecondTimeout = 5;
        int threadPoolCoreSize = Runtime.getRuntime().availableProcessors();
        int threadPoolMaxSize = threadPoolCoreSize * 2;
        int threadPoolCapacity = 1000;

        public Builder overflow(OverflowProcessor overflow) {
            this.overflowProcessor = overflow;
            return this;
        }

        public Builder capacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder batchSize(Integer size) {
            this.batchSize = size;
            return this;
        }

        public Builder threadPoolCore(int threadPoolCoreSize) {
            this.threadPoolCoreSize = threadPoolCoreSize;
            return this;
        }

        public Builder threadPoolMaxSize(int threadPoolMaxSize) {
            this.threadPoolMaxSize = threadPoolMaxSize;
            return this;
        }

        public Builder threadPoolCapacity(int threadPoolCapacity) {
            this.threadPoolCapacity = threadPoolCapacity;
            return this;
        }

        public Builder batchSecondTimeout(int batchSecondTimeout) {
            this.batchSecondTimeout = batchSecondTimeout;
            return this;
        }

        public BatchEventBus build() {
            return new BatchEventBus(this);
        }
    }
}

