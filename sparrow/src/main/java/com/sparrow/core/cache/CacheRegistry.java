package com.sparrow.core.cache;

import com.sparrow.container.FactoryBean;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zh_harry@163.com
 * @date: 2019/6/25 14:26
 * @description:
 */
public class CacheRegistry implements FactoryBean<Cache> {

    private CacheRegistry() {
    }

    private static class Nested {
        private static CacheRegistry instant = new CacheRegistry();
    }

    public static CacheRegistry getInstance() {
        return Nested.instant;
    }

    private Map<String, Cache> registry = new ConcurrentHashMap<>();

    @Override
    public void pubObject(String name, Cache o) {
        registry.put(name, o);
    }

    @Override
    public Cache getObject(String name) {
        return registry.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    public void removeObject(String name) {
        registry.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return registry.keySet().iterator();
    }
}
