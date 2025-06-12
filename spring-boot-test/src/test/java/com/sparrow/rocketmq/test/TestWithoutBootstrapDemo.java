package com.sparrow.rocketmq.test;

import com.sparrow.spring.starter.config.SparrowConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@SpringBootApplication(scanBasePackages = "com.sparrow.*")
public class TestWithoutBootstrapDemo {
    @Autowired
    private SparrowConfig sparrowConfig;

    @Test
    public void testSpring() {
        System.out.println(sparrowConfig);
    }
}
