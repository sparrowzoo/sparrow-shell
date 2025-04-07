package com.sparrow.spring.boot.scan;

import org.springframework.stereotype.Component;

@Component
public class C {
    C() {
        System.err.println("C scan 优先");
    }
}
