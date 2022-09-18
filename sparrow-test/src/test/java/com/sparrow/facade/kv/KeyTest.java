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

package com.sparrow.facade.kv;

import com.sparrow.cache.Key;
import com.sparrow.constant.SparrowModule;

/**
 * Created by harry on 2018/1/8.
 */
public class KeyTest {
    public static void main(String[] args) {
        //module:business.b1.b2.b3:key1:key2.3:key3
        Key.Business od=new Key.Business(SparrowModule.CODE,"OD");
        Key.Business codeIdNamePair = new Key.Business(SparrowModule.CODE, "OD", "NAME", "PAIR");
        Key.Business userRegister = new Key.Business(SparrowModule.USER, "REGISTER","T1");
        Key.Business user = new Key.Business(SparrowModule.USER);

        Key codeIdNamePairKey= new Key.Builder().business(codeIdNamePair).build();
        Key userRegisterKey=new Key.Builder().business(userRegister).businessId(1, 2).build();
        Key userKey=new Key.Builder().business(userRegister.append("times","validate")).businessId(1, 2).build();
        Key odAirline=new Key.Builder().business(od.append("BJS","CHI","HU")).build();
        System.out.println("key---");
        System.out.println(codeIdNamePairKey.key());
        System.out.println(userRegisterKey.key());
        System.out.println(userKey.key());

        System.out.println("business---");
        System.out.println(codeIdNamePairKey.getBusiness());
        System.out.println(userRegisterKey.getBusiness());
        System.out.println(userKey.getBusiness());

        System.out.println("module ---");
        System.out.println(codeIdNamePairKey.getModule());
        System.out.println(userRegisterKey.getModule());
        System.out.println(userKey.getModule());

        System.out.println("key business---");
        System.out.println(Key.parse(codeIdNamePairKey.key()).getBusiness());
        System.out.println(Key.parse(userRegisterKey.key()).getBusiness());
        System.out.println(Key.parse(userKey.key()).getBusiness());

        System.out.println("key key---");
        System.out.println(Key.parse(codeIdNamePairKey.key()).key());
        System.out.println(Key.parse(userRegisterKey.key()).key());
        System.out.println(Key.parse(userKey.key()).key());

        System.out.println("key module---");
        System.out.println(Key.parse(codeIdNamePairKey.key()).getModule());
        System.out.println(Key.parse(userRegisterKey.key()).getModule());
        System.out.println(Key.parse(userKey.key()).getModule());
    }
}
