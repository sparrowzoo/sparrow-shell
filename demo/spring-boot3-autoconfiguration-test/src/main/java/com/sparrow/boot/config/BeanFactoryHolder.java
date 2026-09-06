package com.sparrow.boot.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryHolder implements BeanFactoryPostProcessor, BeanDefinitionRegistryPostProcessor {
    static ConfigurableListableBeanFactory factory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String name : registry.getBeanDefinitionNames()) {
            if (name.startsWith("test") || name.toLowerCase().contains("sparrow")) {
                System.out.println(name);
            }
        }
        System.out.println("BeanDefinition init done");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 这里的执行时机：invokeBeanFactoryPostProcessors（极早期）
        // 此时所有 BeanDefinition 已注册完，但没有任何 Bean 被实例化（包括本类）
        factory = beanFactory;
        System.out.println("【缓存器】BeanFactory 已成功缓存！");
    }
}