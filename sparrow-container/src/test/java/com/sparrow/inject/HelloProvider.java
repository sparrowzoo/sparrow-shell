package com.sparrow.inject;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class HelloProvider {
    @Inject
    private HelloApi helloTest;

    public HelloApi getHelloTest() {
        return helloTest;
    }
}
