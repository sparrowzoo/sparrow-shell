package com.sparrow.rocketmq.test;

import com.sparrow.spring.starter.config.SparrowConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestWithJupter {
    @Autowired
    private SparrowConfig sparrowConfig;

    @Test//注意这里用JUNIT 5 注解
    public void test() {
        System.out.println(sparrowConfig.toString());
    }
}
