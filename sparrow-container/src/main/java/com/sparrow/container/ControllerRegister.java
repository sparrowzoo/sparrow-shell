package com.sparrow.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author by harry
 */
public class ControllerRegister implements FactoryBean<Object> {
    private Map<String,Object> map=new HashMap();

    @Override public void pubObject(String name, Object o) {
        this.map.put(name,o);
    }

    @Override public Object getObject(String name) {
        return this.map.get(name);
    }

    @Override public Class<?> getObjectType() {
        return Object.class;
    }

    @Override public void removeObject(String name) {
        this.map.remove(name);
    }

    @Override public Iterator<String> keyIterator(){
        return this.map.keySet().iterator();
    }
}
