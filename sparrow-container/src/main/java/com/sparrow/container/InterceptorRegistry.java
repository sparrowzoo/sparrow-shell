package com.sparrow.container;

import com.sparrow.protocol.mvc.HandlerInterceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author by harry
 */
public class InterceptorRegistry implements FactoryBean<HandlerInterceptor> {

    private Map<String, HandlerInterceptor> map = new HashMap();

    @Override
    public void pubObject(String name, HandlerInterceptor interceptor) {
        this.map.put(name, interceptor);
    }

    @Override
    public HandlerInterceptor getObject(String name) {
        return this.map.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return HandlerInterceptor.class;
    }

    @Override
    public void removeObject(String name) {
        this.map.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.map.keySet().iterator();
    }
}
