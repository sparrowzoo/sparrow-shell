package com.sparrow.aop;


import com.sparrow.tracer.TracerAccessor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
@Aspect
public class ExecuteDurationInterceptor extends AbstractExecuteDurationInterceptor {

    @Pointcut("@annotation(com.sprucetec.search.common.tracer.ExecuteDuration)")
    public void executeDuration() {
    }

    @Override
    protected TracerAccessor getTracerAccessor(Object arg) {
return null;
    }

    @Override
    protected void alarm(Logger logger, Throwable e, TracerAccessor tracerAccessor) {

    }

    @Override
    protected void alarm(Logger logger, TracerAccessor tracerAccessor, String format, Object... args) {
           }
}