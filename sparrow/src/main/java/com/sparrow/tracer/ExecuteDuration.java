package com.sparrow.tracer;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExecuteDuration {
    /**
     * span name
     *
     * @return
     */
    String spanName() default "";
}