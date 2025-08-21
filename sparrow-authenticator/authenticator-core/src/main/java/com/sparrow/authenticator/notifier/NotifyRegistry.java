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
package com.sparrow.authenticator.notifier;

import com.sparrow.container.FactoryBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotifyRegistry implements FactoryBean<Notifier> {
    private Map<String, Notifier> notifiers = new HashMap<>();

    private NotifyRegistry() {
    }

    public static NotifyRegistry getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    public void pubObject(String name, Notifier o) {
        this.notifiers.put(name, o);
    }

    @Override
    public Notifier getObject(String name) {
        return this.notifiers.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return Notifier.class;
    }

    @Override
    public void removeObject(String name) {
        this.notifiers.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.notifiers.keySet().iterator();
    }

    public static class Inner {
        private static final NotifyRegistry INSTANCE = new NotifyRegistry();
    }
}
