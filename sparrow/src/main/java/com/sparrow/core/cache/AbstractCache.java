package com.sparrow.core.cache;

/**
 * @author: zhanglizhi01@meicai.cn
 * @date: 2019/6/25 18:01
 * @description:
 */
public abstract class AbstractCache<K,V> implements Cache<K,V> {
    private String name;
    public AbstractCache(String name) {
        this.name=name;
        CacheRegistry.getInstance().pubObject(name,this);
    }
    public String getName(){
        return this.name;
    }
}
