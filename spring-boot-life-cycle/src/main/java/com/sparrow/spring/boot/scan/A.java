package com.sparrow.spring.boot.scan;

import com.sparrow.config.C1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({"c4", "c5"})
public class A {
    @Autowired
    private D d;

    @Autowired
    private C1 c1;
    public A() {
        System.err.println("A scan 优先");
    }
}
