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

import com.sparrow.cache.CacheClient;
import com.sparrow.constant.SparrowModule;
import com.sparrow.constant.cache.KEY;
import com.sparrow.container.Container;
import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.exception.CacheConnectionException;
import org.junit.Test;

/**
 * @author by harry
 */
public class RedisKeyTest {

    @Test
    public void readLoc() throws CacheConnectionException {
        KEY.Business business=new KEY.Business(SparrowModule.CODE,"KEY");

        KEY key=new KEY.Builder().business(business).build();

        Container container = new SparrowContainer();
        container.setConfigLocation("/redis_config.xml");
        container.init();
        CacheClient client = container.getBean("cacheClient");
        //client.string().set(key,"test");
        System.out.println(client.key().expire(key,1L));
    }
}
