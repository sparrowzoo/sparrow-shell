package com.sparrow.container;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.constant.SysObjectName;
import com.sparrow.core.TypeConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 2018/2/9.
 * 这里是示例demo具体代码业务端可以实现
 */
public class SpringContainerImpl implements Container {

    private ApplicationContext applicationContext;

    @Override public FactoryBean getSingletonRegister() {
        return null;
    }

    @Override public FactoryBean getControllerRegister() {
        return null;
    }

    @Override public FactoryBean getProxyBeanRegister() {
        return null;
    }

    @Override public FactoryBean getInterceptorRegister() {
        return null;
    }

    @Override
    public MethodAccessor getProxyBean(Class<?> clazz) {
        return null;
    }

    @Override
    public List<TypeConverter> getFieldList(Class clazz) {
        return null;
    }

    @Override
    public Map<String, Method> getControllerMethod(String clazzName) {
        return null;
    }

    @Override
    public <T> T getBean(String beanName) {
        return (T)applicationContext.getBean(beanName);
    }

    @Override
    public <T> T getBean(SysObjectName sysObjectName) {
        return null;
    }

    @Override public void init(ContainerBuilder builder) {

    }

}
