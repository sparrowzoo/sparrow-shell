package com.sparrow.inject;

import jakarta.inject.Named;

@Named
public class HelloTest implements HelloApi {
    public void print() {
        System.out.println("hello");
    }
}
