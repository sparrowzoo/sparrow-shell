package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author by harry
 */
public class SimpleProxyBeanRegistry implements FactoryBean<MethodAccessor> {
    private Map<String, MethodAccessor> map = new HashMap<>();

    @Override public void pubObject(String name, MethodAccessor o) {
        this.map.put(name, o);
    }

    @Override public MethodAccessor getObject(String name) {
        return this.map.get(name);
    }

    @Override public Class<?> getObjectType() {
        return MethodAccessor.class;
    }

    @Override public void removeObject(String name) {
        this.map.remove(name);
    }


    @Override public Iterator<String> keyIterator(){
        return this.map.keySet().iterator();
    }
}
