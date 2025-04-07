package com.sparrow.spring.boot.scan;

import org.springframework.stereotype.Component;

@Component
public class B {
    B() {
        System.err.println("B scan 优先");
    }
}
