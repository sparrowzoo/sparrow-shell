package com.sparrow.inject;

import javax.inject.Named;

//@Named
public class HelloTest implements HelloApi {
    public void print() {
        System.out.println("hello");
    }
}
