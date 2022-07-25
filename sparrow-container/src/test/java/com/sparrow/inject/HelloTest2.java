package com.sparrow.inject;

import javax.inject.Named;

@Named
public class HelloTest2 implements HelloApi {
    public void print() {
        System.out.println("hello2");
    }
}
