package com.sparrow.spring.boot.scan;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("scan2")
public class Scan3 {
    Scan3() {
        System.err.println("SCAN3 DEPENDS ON SCAN2");
    }
}
