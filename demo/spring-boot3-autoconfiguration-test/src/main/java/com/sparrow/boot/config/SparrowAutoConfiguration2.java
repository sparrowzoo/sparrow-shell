package com.sparrow.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@Slf4j
//@AutoConfigureBefore(SparrowAutoConfiguration1.class)
public class SparrowAutoConfiguration2 {


    // ===========================================================
    // 2. 你的 static 块（此时缓存器已经执行完了）
    // ===========================================================
    static {
        // 直接拿缓存器里的静态工厂
        ConfigurableListableBeanFactory factory = BeanFactoryHolder.factory;

        if (factory != null) {
            String[] names = factory.getBeanDefinitionNames();
            log.info("================== 所有 BeanDefinition 列表 (共 {} 个) ==================", names.length);

            // 打印全部的 BeanDefinition 名称（如果太多，你可以只打印前20个）
            log.info("全部名称: {}", Arrays.toString(names));

            // 验证你想看的关键点
            log.info("是否包含 'sparrowAutoConfiguration1' ? {}", factory.containsBeanDefinition("sparrowAutoConfiguration1"));
            log.info("是否包含 'sparrowAutoConfiguration2' ? {}", factory.containsBeanDefinition("sparrowAutoConfiguration2"));
            log.info("=======================================================================");
        } else {
            // 正常情况下这里永远不会进去，因为 postProcessBeanFactory 在 onRefresh 之前就执行了
            log.warn("警告：BeanFactory 尚未缓存（理论上不会发生）");
        }

        log.info("SparrowAutoConfiguration2 static init");
    }

    public SparrowAutoConfiguration2() {
        log.info("Sparrow Auto Configuration2 INIT");
    }

    @Bean
    public TestFilter test2() {
        return new TestFilter();
    }

}
