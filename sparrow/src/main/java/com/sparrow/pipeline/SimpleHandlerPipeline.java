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
package com.sparrow.pipeline;

import com.sparrow.concurrent.SparrowThreadFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class SimpleHandlerPipeline implements HandlerPipeline {

    private static final int CPU_CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService consumerThreadPool = new ThreadPoolExecutor(CPU_CORE_SIZE, CPU_CORE_SIZE * 2,
            60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(CPU_CORE_SIZE * 32),
            new SparrowThreadFactory.Builder().namingPattern("pipeline-async-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());


    public SimpleHandlerPipeline(boolean reverse) {
        this.reverse = reverse;
    }

    public SimpleHandlerPipeline() {
        this.reverse = false;
    }

    private HandlerContext head;
    private HandlerContext tail;
    private AtomicInteger asyncCount = new AtomicInteger(0);

    private boolean reverse;

    @Override
    public AtomicInteger getAsyncCount() {
        return this.asyncCount;
    }

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public void add(Handler handler) {
        this.add(handler, false);
    }

    private void add(Handler handler, boolean asyc) {
        HandlerContext handlerContext = new HandlerContext(this, handler, asyc);
        handlerContext.name = handler.getClass().getSimpleName();
        if (head == null) {
            head = handlerContext;
            tail = handlerContext;
            return;
        }

        handlerContext.prev = tail;
        tail.next = handlerContext;
        tail = handlerContext;
    }

    @Override
    public void addAsync(Handler handler) {
        this.add(handler, true);
        this.asyncCount.incrementAndGet();
    }

    private boolean hashException(PipelineData arg) {
        Map<String, Object> result = arg.getResult();
        for (String handlerName : result.keySet()) {
            Object o = result.get(handlerName);
            if (o instanceof Exception) {
                Exception e = (Exception) o;
                if (arg.isThrowWhenException()) {
                    throw (RuntimeException) e;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void fire(PipelineData arg) throws InterruptedException {
        PipelineAsyncData pipelineAsyncData = null;
        if (asyncCount.get() > 0 && arg instanceof PipelineAsyncData) {
            pipelineAsyncData = (PipelineAsyncData) arg;
            pipelineAsyncData.initLatch(this.asyncCount.get());
        }
        if (!reverse) {
            head.fire(arg);
        } else {
            tail.fire(arg);
        }

        if (pipelineAsyncData != null) {
            pipelineAsyncData.getCountDownLatch().await();
            if (arg.isThrowWhenException()) {
                this.hashException(arg);
            }
        }
    }

    public ExecutorService getConsumerThreadPool() {
        return consumerThreadPool;
    }
}
