package com.sparrow.inject;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class HelloProvider {
    @Inject
    @Named("helloTest2")
    private HelloApi helloTest;

    public void setHelloTest(HelloTest helloTest) {
        this.helloTest = helloTest;
    }

    public HelloApi getHelloTest() {
        return helloTest;
    }
}
