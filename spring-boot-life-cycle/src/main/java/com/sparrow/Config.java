package com.sparrow;

import com.sparrow.config.C1;
import com.sparrow.config.C2;
import com.sparrow.config.C5;
import org.springframework.context.annotation.Bean;

public class Config {
    @Bean
    public C1 c1(){
        return new C1();
    }

    @Bean

    public C2 c2(){
        return new C2();
    }

    @Bean
    public C5 c5(){
        return new C5();
    }
}
