package com.sparrow.inject;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.cache.CacheRegistry;
import com.sparrow.core.cache.StringSoftExpirableCache;
import com.sparrow.core.spi.ApplicationContext;


/**
 * @author by harry
 */
public class SparrowContainerTest {

    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder());
        HelloProvider helloProvider = container.getBean("helloProvider");
        helloProvider.getHelloTest().print();
        StringSoftExpirableCache cache = (StringSoftExpirableCache) CacheRegistry.getInstance().getObject("action-url-cache");
        System.out.printf(cache.getName());
    }
}
