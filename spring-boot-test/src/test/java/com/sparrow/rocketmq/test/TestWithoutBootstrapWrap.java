package com.sparrow.rocketmq.test;

import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.test.TestWithoutBootstrap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TestWithoutBootstrap(scanBasePackages = "com.sparrow.*")
public class TestWithoutBootstrapWrap {
    @Autowired
    private SparrowConfig sparrowConfig;

    @Test
    public void testSpring() {
        System.out.println(sparrowConfig);
    }
}
