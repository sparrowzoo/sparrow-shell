package com.sparrow.xml;

import com.sparrow.container.BeanDefinitionParserDelegate;
import com.sparrow.container.BeanDefinitionReader;
import com.sparrow.container.SimpleBeanDefinitionRegistry;
import com.sparrow.container.XmlBeanDefinitionReader;

/**
 * @author by harry
 */
public class BeanDefinitionTest {
    public static void main(String[] args) throws Exception {
        SimpleBeanDefinitionRegistry registry=new SimpleBeanDefinitionRegistry();

        BeanDefinitionParserDelegate delegate=new BeanDefinitionParserDelegate();

        BeanDefinitionReader definitionReader=new XmlBeanDefinitionReader(registry,null,delegate);
        definitionReader.loadBeanDefinitions("/beans.xml");
        System.out.println(definitionReader.getRegistry());


    }
}
