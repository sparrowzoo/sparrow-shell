package com.sparrow.pipeline;

/**
 * @author by harry
 */
public interface Handler<T>{
    void invoke(T arg);
}
