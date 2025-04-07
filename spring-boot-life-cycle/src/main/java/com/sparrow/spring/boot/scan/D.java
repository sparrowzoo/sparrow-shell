package com.sparrow.spring.boot.scan;

import org.springframework.stereotype.Component;

@Component
public class D {
    D() {
        System.err.println("D 被依赖优先");
    }
}
