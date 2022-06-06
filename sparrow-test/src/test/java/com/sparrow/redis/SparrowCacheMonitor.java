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

import com.sparrow.cache.CacheMonitor;
import com.sparrow.concurrent.AbstractLock;
import com.sparrow.constant.cache.KEY;

/**
 * Created by harry on 2018/1/25.
 */
public class SparrowCacheMonitor implements CacheMonitor{
    @Override
    public boolean before(Long startTime, KEY key) {
        //System.out.println("start time"+startTime+" key"+key.key());
        return true;
    }

    @Override
    public void monitor(Long startTime, Long endTime, KEY key) {
        //System.out.println("module-"+key.getModule()+" business.type-"+key.getBusiness()+" key-"+key.key()+" start.time-"+startTime+" end.time-"+endTime);
    }

    @Override
    public void penetrate(KEY key) {
        new AbstractLock(){
            @Override
            protected Boolean readLock(String key) {
                //这里读性能指标，如果OK则true，如果不OK则返回false
                System.out.println("check 指标true");
                return true;
            }
        }.retryAcquireLock(key.key());
    }
}
