package com.sparrow.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: zh_harry@163.com
 * @date: 2019/6/25 14:14
 * @description:
 */
public class StrongDurationCache<K, V> extends AbstractCache<K, V> {

    private Map<K, V> cache = new ConcurrentHashMap<>();

    public StrongDurationCache(String name) {
        super(name);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<K, V> map) {
        cache.putAll(map);
    }

    @Override
    public V getIfPresent(K key) {
        return cache.get(key);
    }

    @Override
    public Map<K, V> getAllPresent(Iterable<K> keys) {
        Map<K, V> map = new HashMap<>();
        for (K k : keys) {
            map.put(k, cache.get(k));
        }
        return map;
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return new ConcurrentHashMap<>(cache);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void remove(K name) {
        cache.remove(name);
    }
}
