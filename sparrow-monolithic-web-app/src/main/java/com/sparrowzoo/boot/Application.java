package com.sparrowzoo.boot;

import com.sparrow.chat.boot.config.EnableChatApp;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.file.config.EnableFileApp;
import com.sparrow.passport.config.EnablePassport;
import com.sparrowzoo.coder.boot.config.EnableCoderApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
(scanBasePackages = {"com.sparrowzoo"})
@EnableFileApp
@EnablePassport
@EnableCoderApp
@EnableChatApp
//@MapperScan(basePackages = "com.sparrow.coder.dao")
public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        /**
         * 在spring 容器启动前 通过sparrow容器提供proxy 代理类反射加速
         *
         * 因为orm template 初始化时需要method accessor 提速
         */
        springApplication.addListeners(new ApplicationListener<ApplicationStartingEvent>() {
            @Override
            public void onApplicationEvent(ApplicationStartingEvent event) {
                Container container = ApplicationContext.getContainer();
                //只提供proxy 代码类加速反射
                ContainerBuilder builder = new ContainerBuilder()
                        //只扫描com.sparrow下的类
                        .scanBasePackage("com.sparrow,com.sparrowzoo")
                        .initController(false)
                        .initSingletonBean(false)
                        .initProxyBean(true)
                        .initInterceptor(false);
                container.init(builder);
            }
        });

        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
                log.info("application startup at {}", contextRefreshedEvent.getTimestamp());
            }
        });
        springApplication.addListeners(new ApplicationListener<ContextClosedEvent>() {
            @Override
            public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
                log.info("application closed at at {}", contextClosedEvent.getTimestamp());
            }
        });
        springApplication.run(args);
    }
}
