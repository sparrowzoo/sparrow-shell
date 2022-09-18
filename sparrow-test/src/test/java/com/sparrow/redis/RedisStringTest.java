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
import com.sparrow.cache.CacheDataNotFound;
import com.sparrow.cache.Key;
import com.sparrow.cache.exception.CacheConnectionException;
import com.sparrow.container.Container;
import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.protocol.ModuleSupport;

/**
 * @author by harry
 */
public class RedisStringTest {
    private static CacheClient client;
    public static void main(String[] args) throws CacheConnectionException {
        Container container = new SparrowContainer();
        //定义模块，一个业务会存在多个模块
        ModuleSupport OD = new ModuleSupport() {
            @Override
            public String code() {
                return "01";
            }

            @Override
            public String name() {
                return "OD";
            }
        };

        //相同模块下会存在多个业务
        Key.Business od = new Key.Business(OD, "POOL");
        //container.setContextConfigLocation("/redis_config.xml");

        container.init();
        client = container.getBean("cacheClient");
        //相同业务下存在多个KEY
        Key key = new Key.Builder().business(od).businessId("BJS", "CHI", "HU").build();

        client.string().set(key, "test");
        System.out.println(client.string().get(key));
        client.key().delete(key);
        String value = client.string().get(key, new CacheDataNotFound<String>() {
            @Override
            public String read(Key key) {
                return "from db";
            }
        });
        System.out.println(value);
        client.string().append(key, "append value");
        System.out.println(client.string().get(key));
        client.string().set(key, 0);
        client.string().increase(key);
        System.out.println(client.string().get(key));

        client.string().increase(key, 10L);
        System.out.println(client.string().get(key));

        client.string().decrease(key, 10L);
        System.out.println(client.string().get(key));

        client.string().decrease(key);
        System.out.println(client.string().get(key));

        client.string().setExpire(key, 10, "11111");
        System.out.println(client.string().get(key));

        client.key().delete(key);
        boolean count = client.string().setIfNotExist(key, "not exist");
        count = client.string().setIfNotExist(key, "not exist");
        System.out.println(count);
        System.out.println(client.string().get(key));
        client.key().delete(key);
        System.out.println(client.string().get(key));

        client.key().delete(key);
        client.string().set(key, new RedisEntity(1, "ZHANGSAN"));
        System.out.println(client.string().get(key));

        Key k2 = Key.parse("OD.POOL:BJS.CHI.HU");
        System.out.println("key:" + k2.key() + ",module:" + k2.getModule() + " business:" + k2.getBusiness());
    }
}
