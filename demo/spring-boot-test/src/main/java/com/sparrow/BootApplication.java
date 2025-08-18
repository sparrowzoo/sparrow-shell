/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.filter.TestFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
//        (scanBasePackages = "com.sparrow.*")
//@EnableDiscoveryClient
@MapperScan("com.sparrow")
public class BootApplication {
    private static Logger log = LoggerFactory.getLogger(BootApplication.class);

    @Bean
    public ServletContextInitializer registerFilters() {
        log.info("init filter register ServletContextInitializer....");
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                log.info("ServletContextInitializer add filter ....");
                servletContext.addFilter("test", TestFilter.class).addMappingForUrlPatterns(null, false, "/*");
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BootApplication.class);

        springApplication.addListeners(new ApplicationListener<ApplicationContextInitializedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationContextInitializedEvent event) {
                log.info("ApplicationContextInitializedEvent at {}", System.currentTimeMillis());
            }
        });

        springApplication.addListeners(new ApplicationListener<ApplicationStartedEvent>() {
            @Override
            public void onApplicationEvent(ApplicationStartedEvent event) {
                log.info("ApplicationStartedEvent  at {}", System.currentTimeMillis());
            }
        });
        springApplication.addListeners(new ApplicationListener<ApplicationStartingEvent>() {
            @Override
            public void onApplicationEvent(ApplicationStartingEvent event) {
                Container container = ApplicationContext.getContainer();
                ContainerBuilder builder = new ContainerBuilder().scanBasePackage("com.sparrow").initController(false).initSingletonBean(false).initInterceptor(false);
                container.init(builder);
                log.info("ApplicationStartingEvent at {}", System.currentTimeMillis());
            }
        });


        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
                log.info("ContextRefreshedEvent at {}", contextRefreshedEvent.getTimestamp());
            }
        });
        springApplication.addListeners(new ApplicationListener<ContextClosedEvent>() {

            @Override
            public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
                log.info("ContextClosedEvent at {}", contextClosedEvent.getTimestamp());
            }
        });
        springApplication.run(args);
    }
}

