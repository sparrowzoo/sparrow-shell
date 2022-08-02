package com.sparrow.inject;

import com.sparrow.container.Container;
import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.core.spi.ApplicationContext;
import javax.inject.Named;

/**
 * @author by harry
 */
public class SparrowContainerTest {

    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.init();
        HelloProvider helloProvider = container.getBean("helloProvider");
        helloProvider.getHelloTest().print();
    }
}
