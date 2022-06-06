package com.sparrow.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public interface Cache<K, V> {
    String getName();

    V get(K key);

    void put(K key, V value);

    void putAll(Map<K,V> map);

    V getIfPresent(K key);

    Map<K, V> getAllPresent(Iterable<K> keys);

    long size();

    ConcurrentMap<K, V> asMap();

    void clear();

    void remove(K name);
}
