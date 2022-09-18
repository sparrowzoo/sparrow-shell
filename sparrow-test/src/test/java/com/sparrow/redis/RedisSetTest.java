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
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.ModuleSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by harry on 2018/1/26.
 */
public class RedisSetTest {
    public static void main(String[] args) throws CacheConnectionException {
        Container container = ApplicationContext.getContainer();
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
        Key key = new Key.Builder().business(od).businessId("BJS", "CHI", "HU").build();
        container.setContextConfigLocation("/sparrow_application_context.xml");
        container.init();
        CacheClient client = container.getBean("cacheClient");
        client.key().delete(key);
        client.set().add(key, 1);
        client.set().add(key, "1", "2", "3", "4", "end");
        System.out.println(client.set().getSize(key));

        List<Object> list = new ArrayList<Object>();
        list.add("s1");
        list.add("s2");
        list.add("s3");
        client.set().add(key, list);
        System.out.println(client.set().getSize(key));

        client.key().delete(key);
        Set<String> fromdb = client.set().list(key, new CacheDataNotFound<Set<String>>() {
            @Override
            public Set<String> read(Key key) {
                Set<String> set = new HashSet<String>();
                set.add("from db");
                return set;
            }
        });

        for (String db : fromdb) {
            System.out.println(db);
        }

        client.key().delete(key);
        Set<RedisEntity> set = new HashSet<RedisEntity>();
        set.add(new RedisEntity(1, "1"));
        client.set().add(key, set);
        set = client.set().list(key, RedisEntity.class);
        for (RedisEntity re : set) {
            System.out.println(re.getId() + "-" + re.getName());
        }
    }
}
