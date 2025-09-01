package com.sparrow.spring;

import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import com.sparrowzoo.boot.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class TestWithBoostrap {

    @Autowired
    private SparrowConfig sparrowConfig;

    @Test
    public void testSpring() {
        System.out.println(sparrowConfig.toString());
        System.out.println("hello sparrows");
    }
}
