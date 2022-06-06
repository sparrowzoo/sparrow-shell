package com.sparrow.container;

/**
 * generator a new bean with class
 *
 * @param <T>
 */
public interface ClassFactoryBean<T> extends FactoryBean<T> {
    void pubObject(Class name, T o);

    T getObject(Class name);
}
