package com.sparrow.protocol.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodOrder {

    /**
     * 指定方法的顺序
     *
     * @return 该属性的顺序
     */
    float order();
}