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

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by harry on 2018/1/26.
 */
public class RedisOrderSetTest {
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
        Key key = new Key.Builder().business(od).businessId("BJS", "CHI", "HU","ORDER_SET").build();

        container.setConfigLocation("/redis_config.xml");
        container.init();
        CacheClient client = container.getBean("cacheClient");
        client.key().delete(key);
        client.sortedSet().add(key, "field",1L);

        client.key().delete(key);
        System.out.println(client.sortedSet().getSize(key));

        Map<String,Double> set = new TreeMap<String, Double>();
        set.put("k1",1d);
        set.put("k2",2d);
        set.put("k3",3d);
        client.sortedSet().putAllWithScore(key, set);

        Map<String,Double> kv=client.sortedSet().getAllWithScore(key);

        for (String db : kv.keySet()) {
            System.out.println(db);
            System.out.println(kv.get(db));
        }

        System.out.println(client.sortedSet().getSize(key));
        client.key().delete(key);
        Map<String,Double> fromdb = client.sortedSet().getAllWithScore(key,String.class, new CacheDataNotFound<Map<String,Double>>() {
            @Override
            public Map<String,Double> read(Key key) {
                Map<String,Double> set = new TreeMap<String, Double>();
                set.put("field",11111d);
                set.put("field2",2222d);
                set.put("field3",33333d);
                return set;
            }
        });

        for (String db : fromdb.keySet()) {
            System.out.println(db);
            System.out.println(fromdb.get(db));
        }
        client.key().delete(key);
    }
}
