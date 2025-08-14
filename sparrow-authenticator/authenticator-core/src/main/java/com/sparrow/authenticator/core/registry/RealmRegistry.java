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

package com.sparrow.authenticator.core.registry;

import com.sparrow.authenticator.core.Realm;
import com.sparrow.container.FactoryBean;

import java.util.Iterator;
import java.util.Map;

public class RealmRegistry implements FactoryBean<Realm> {
    private Map<String, Realm> registry = new java.util.HashMap<>();

    @Override
    public void pubObject(String name, Realm o) {
        this.registry.put(name, o);
    }

    @Override
    public Realm getObject(String name) {
        return this.registry.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return Realm.class;
    }

    @Override
    public void removeObject(String name) {
        this.registry.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.registry.keySet().iterator();
    }
}
