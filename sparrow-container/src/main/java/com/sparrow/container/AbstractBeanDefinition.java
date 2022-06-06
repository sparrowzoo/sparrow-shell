package com.sparrow.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author by harry
 */
public class AbstractBeanDefinition implements BeanDefinition {

    private boolean singleton;
    private boolean prototype;
    private boolean controller;
    private boolean interceptor;
    private String alias;
    private TreeMap<Integer, ValueHolder> constructorArgsMap = new TreeMap<>();
    private Class[] constructorTypes;
    private Object[] constructorArgs;
    private List<ValueHolder> propertyList = new ArrayList<>();

    private Map<String, ValueHolder> placeholder = new HashMap();
    private String className;
    private String scope;


    public void addProperty(ValueHolder valueHolder) {
        this.propertyList.add(valueHolder);
    }

    public void addPlaceholder(String placeholder, ValueHolder valueHolder) {
        this.placeholder.put(placeholder, valueHolder);
    }

    public void addConstructorArg(int index, ValueHolder valueHolder) {
        this.constructorArgsMap.put(index, valueHolder);
    }

    @Override
    public String getBeanClassName() {
        return this.className;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public TreeMap<Integer, ValueHolder> getConstructorArgsMap() {
        return this.constructorArgsMap;
    }

    @Override
    public List<ValueHolder> getPropertyValues() {
        return this.propertyList;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public boolean isPrototype() {
        return prototype;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean isInterceptor() {
        return interceptor;
    }

    @Override
    public String alias() {
        return this.alias;
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }
}
