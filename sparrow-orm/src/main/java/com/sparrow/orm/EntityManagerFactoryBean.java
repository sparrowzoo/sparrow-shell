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
package com.sparrow.orm;

import com.sparrow.constant.CacheKey;
import com.sparrow.container.ClassFactoryBean;
import com.sparrow.core.cache.Cache;
import com.sparrow.core.cache.StrongDurationCache;
import com.sparrow.utility.ClassUtility;

import java.util.Iterator;
import java.util.Map;

public class EntityManagerFactoryBean implements ClassFactoryBean<EntityManager> {
    private static class Nested {
        private static EntityManagerFactoryBean single = new EntityManagerFactoryBean();
    }

    public static EntityManagerFactoryBean getInstance() {
        return Nested.single;
    }

    private Cache<String, EntityManager> cache = new StrongDurationCache<>(CacheKey.ORM);

    @Override
    public void pubObject(String name, EntityManager o) {
        cache.put(name, o);
    }

    @Override
    public EntityManager getObject(String name) {
        return cache.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return EntityManager.class;
    }

    @Override
    public void removeObject(String name) {
        cache.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        Map<String, EntityManager> map = cache.asMap();
        if (map == null) {
            return null;
        }
        return map.keySet().iterator();
    }

    @Override
    public void pubObject(Class clazz, EntityManager o) {
        cache.put(ClassUtility.getEntityNameByClass(clazz), o);
    }

    @Override
    public EntityManager getObject(Class clazz) {
        return cache.get(ClassUtility.getEntityNameByClass(clazz));
    }
}
