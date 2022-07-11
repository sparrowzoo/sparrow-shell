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

@SuppressWarnings("unchecked")
class HandlerContext<T> {
    HandlerPipeline pipeline;
    private String name;
    HandlerContext next;
    HandlerContext prev;
    boolean async;

    public HandlerContext(HandlerPipeline pipeline, Handler handler, boolean async) {
        this.name = handler.getClass().getSimpleName();
        this.pipeline = pipeline;
        this.handler = handler;
        this.async = async;
    }

    public HandlerContext(HandlerPipeline pipeline, Handler handler) {
        this(pipeline, handler, false);
    }

    private Handler handler;

    private void innerFire(Object arg) {
        if (this.async) {
            PipelineAsyncData pipelineAsync = (PipelineAsyncData) arg;
            pipeline.getConsumerThreadPool().submit(new Runnable() {
                @Override
                public void run() {
                    handler.invoke(pipelineAsync);
                    pipelineAsync.getCountDownLatch().countDown();
                }
            });
            return;
        }
        handler.invoke(arg);
    }

    public void fire(T arg) {
        this.innerFire(arg);
        if (!pipeline.isReverse()) {
            if (next != null) {
                next.fire(arg);
            }
            return;
        }
        if (prev != null) {
            prev.fire(arg);
        }
    }
}
