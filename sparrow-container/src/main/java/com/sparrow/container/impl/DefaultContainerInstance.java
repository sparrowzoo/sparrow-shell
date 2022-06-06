package com.sparrow.container.impl;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.support.Initializer;

public class DefaultContainerInstance {
    private Container container;

    public Container getContainer() {
        return container;
    }

    public DefaultContainerInstance() {
        this.container = ApplicationContext.getContainer();
        container.init();
    }

    public Initializer getInitializer() {
        return container.getBean("initializer");
    }
}
