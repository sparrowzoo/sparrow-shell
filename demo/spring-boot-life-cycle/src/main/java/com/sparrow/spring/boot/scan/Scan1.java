package com.sparrow.spring.boot.scan;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({"c4", "c5"})
public class Scan1 {
    public Scan1() {
        System.err.println("scan1 依赖 C4 C5");
    }
}
