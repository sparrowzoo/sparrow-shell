package com.sparrow.spring.boot.scan;

import com.sparrow.config.C3;
import com.sparrow.config.C4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScanConfig
{
    @Bean
    C3 c3(){
        return new C3();
    }

    @Bean
    C4 c4(){
        return new C4();
    }
}
