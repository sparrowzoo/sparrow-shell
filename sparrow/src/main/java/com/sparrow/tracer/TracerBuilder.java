package com.sparrow.tracer;


import com.sparrow.tracer.impl.TracerImpl;

public class TracerBuilder {
    public static Tracer startTracer(String traceId,long timeoutThreshold) {
        return new TracerImpl(traceId,timeoutThreshold);
    }
    public static Tracer startTracer(String traceId) {
        return new TracerImpl(traceId);
    }

    public static Tracer startTracer() {
        return new TracerImpl();
    }
    public static Tracer startTracer(long timeThreshold) {
        return new TracerImpl(null,timeThreshold);
    }
}
