package com.sparrow.authenticator.notifier;

import com.sparrow.container.FactoryBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NotifyRegistry implements FactoryBean<Notifier> {
    private Map<String, Notifier> notifiers = new HashMap<>();

    private NotifyRegistry() {
    }

    public static NotifyRegistry getInstance() {
        return Inner.instance;
    }

    @Override
    public void pubObject(String name, Notifier o) {
        this.notifiers.put(name, o);
    }

    @Override
    public Notifier getObject(String name) {
        return this.notifiers.get(name);
    }

    @Override
    public Class<?> getObjectType() {
        return Notifier.class;
    }

    @Override
    public void removeObject(String name) {
        this.notifiers.remove(name);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.notifiers.keySet().iterator();
    }

    public static class Inner {
        private static final NotifyRegistry instance = new NotifyRegistry();
    }
}
