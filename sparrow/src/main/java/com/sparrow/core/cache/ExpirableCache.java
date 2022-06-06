package com.sparrow.core.cache;

/**
 * @author by harry
 */
public interface ExpirableCache<K,V> extends Cache<K,V> {

    void invalidate(K key);

    void invalidateAll(Iterable<K> keys);

    void invalidateAll();

    void put(K key,V value,int expire);

    V get(K key, LocalCacheNotFound<K, V> hook, int expire);

    void  continueKey(K key);
}
