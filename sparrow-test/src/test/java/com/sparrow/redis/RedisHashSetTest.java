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
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.protocol.ModuleSupport;

/**
 * Created by harry on 2018/1/26.
 */
public class RedisHashSetTest {
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
        Key key = new Key.Builder().business(od).businessId("BJS", "CHI", "HU","HASH").build();

        ContainerBuilder builder=new ContainerBuilder().contextConfigLocation("/dao.xml");
        container.init(builder);

        CacheClient client = container.getBean("cacheClient");



//        client.key().delete(key);
//        client.hash().put(key, "field",1);
//
//        client.key().delete(key);
//        System.out.println(client.hash().getSize(key));
//
//        Map<Integer,RedisEntity> set = new TreeMap<Integer, RedisEntity>();
//        set.put(1,new RedisEntity(1,"k1"));
//        set.put(2,new RedisEntity(2,"k2"));
//        set.put(3,new RedisEntity(3,"k3"));
//        client.hash().put(key, set);
//
//        Map<Integer,RedisEntity> kv=client.hash().getAll(key,Integer.class,RedisEntity.class);
//
//        for (Integer db : kv.keySet()) {
//            System.out.println(db);
//            System.out.println(kv.get(db).getName());
//        }
//
//        System.out.println(client.hash().getSize(key));
//        client.key().delete(key);
//        Map<String,RedisEntity> fromdb = client.hash().getAll(key,String.class,RedisEntity.class, new CacheDataNotFound<Map<String,RedisEntity>>() {
//            @Override
//            public Map<String,RedisEntity> read(KEY key) {
//                Map<String,RedisEntity> set = new TreeMap<String, RedisEntity>();
//                set.put("field",new RedisEntity(1,"f1"));
//                set.put("field2",new RedisEntity(2,"f2"));
//                set.put("field3",new RedisEntity(3,"f3"));
//                return set;
//            }
//        });
//
//        for (String db : fromdb.keySet()) {
//            System.out.println(db);
//            System.out.println(fromdb.get(db).getName());
//        }
//
//        client.key().delete(key);

    }
}
