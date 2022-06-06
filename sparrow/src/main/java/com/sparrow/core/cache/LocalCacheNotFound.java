package com.sparrow.core.cache;

public interface LocalCacheNotFound <K,V> {
    V read(K key);
}