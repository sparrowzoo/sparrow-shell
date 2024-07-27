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

import java.util.concurrent.CountDownLatch;

public class DefaultAsynPipelineData extends DefaultPipelineData implements PipelineAsyncData{

    private CountDownLatch countDownLatch;
    @Override
    public void initLatch(int count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    @Override
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    @Override
    public void latch() throws InterruptedException {
        countDownLatch.await();
    }
}
