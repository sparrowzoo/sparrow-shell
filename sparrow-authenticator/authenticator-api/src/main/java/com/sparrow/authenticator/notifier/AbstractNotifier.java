package com.sparrow.authenticator.notifier;

import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractNotifier<T> implements Notifier<T>, InitializingBean {
    private NotifyRegistry registry = NotifyRegistry.getInstance();

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registry.pubObject(this.getType(), this);
    }
}
