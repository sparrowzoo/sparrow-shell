package com.sparrow.jdk.threadlocal;

import com.sparrow.tracer.Span;
import com.sparrow.tracer.Tracer;
import com.sparrow.tracer.TracerBuilder;
import com.sparrow.tracer.impl.SpanImpl;
import com.sparrow.tracer.impl.TracerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by harry
 */
public class ThreadLocalGc {

    /**
     * -Xmx40m -Xms40m -Xmn20m -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly
     *jstat -gcutil pid 1000 1000
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        for (;;) {
            Tracer tracer=TracerBuilder.startTracer();
//            ((TracerImpl)tracer).cursor().put(Thread.currentThread().getId(),new SpanImpl(null,System.currentTimeMillis(),"sss"));
        }
    }
}
