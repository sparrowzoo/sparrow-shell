package com.sparrow.spring.boot.scan;

import com.sparrow.config.C3;
import com.sparrow.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@AutoConfigureAfter({Config.class,ScanConfig2.class})
public class ScanConfig {
    @Bean
    @DependsOn("c4")
    C3 c3() {
        return new C3();
    }

    @Bean
    @ConditionalOnMissingBean(Scan4.class)
    public Scan4 scan4() {
        System.err.println("scan4 bean created by scanConfig");
        return new Scan4();
    }
}
