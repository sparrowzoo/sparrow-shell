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
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.constant.SparrowModule;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.impl.SparrowContainer;
import org.junit.Test;

/**
 * @author by harry
 */
public class RedisKeyTest {

    @Test
    public void readLoc() throws CacheConnectionException {
        Key.Business business=new Key.Business(SparrowModule.CODE,"KEY");

        Key key=new Key.Builder().business(business).build();

        Container container = new SparrowContainer();
        ContainerBuilder builder=new ContainerBuilder().contextConfigLocation("/dao.xml");
        container.init(builder);
        CacheClient client = container.getBean("cacheClient");
        //client.string().set(key,"test");
        System.out.println(client.key().expireSeconds(key,1L));
    }
}
