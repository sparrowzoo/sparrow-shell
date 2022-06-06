package com.sparrow.xml;

import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author by harry
 */
public class SpringXmlTest {
    public static void main(String[] args) {
            SimpleBeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
            Resource resource = new ClassPathResource("/spring_rocket_mq_consumer.xml");
            new XmlBeanDefinitionReader(registry).loadBeanDefinitions(resource);
    }
}
