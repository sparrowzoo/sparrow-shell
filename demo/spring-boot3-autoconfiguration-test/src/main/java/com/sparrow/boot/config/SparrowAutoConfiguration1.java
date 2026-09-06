package com.sparrow.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;


@Slf4j
public class SparrowAutoConfiguration1 {
    static {
        log.info("SparrowAutoConfiguration1 static init");
    }
    public SparrowAutoConfiguration1() {
        log.info("Sparrow Auto Configuration1 INIT");
    }

    @Bean
    public Test test() {
        return new Test();
    }

}
