package com.sparrow.inject;

import com.sparrow.container.Container;
import com.sparrow.container.impl.SparrowContainer;

/**
 * @author by harry
 */
public class SparrowContainerTest {

    public static void main(String[] args) throws Exception {
        Container container = new SparrowContainer();
        container.init();
        container.getBean("hello2");
    }
}
