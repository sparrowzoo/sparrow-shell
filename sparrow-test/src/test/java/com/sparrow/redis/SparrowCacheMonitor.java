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
import com.sparrow.cache.Key;
import javax.inject.Named;

@Named("cacheMonitor")
public class SparrowCacheMonitor implements CacheMonitor {
    @Override
    public boolean before(Long startTime, Key key) {
        key.getBusiness();
        key.getModule();

        //判断某模块或某业务是否达到阀值等逻辑
        //System.out.println("start time"+startTime+" key"+key.key());
        return true;
    }

    @Override
    public void monitor(Long startTime, Long endTime, Key key) {
        //对某业务和模块进行监控埋点
        //System.out.println("module-"+key.getModule()+" business.type-"+key.getBusiness()+" key-"+key.key()+" start.time-"+startTime+" end.time-"+endTime);
    }

    @Override
    public void breakdown(Key key) {
        //某业务和模块的单位时间内的击穿次数
    }
}
