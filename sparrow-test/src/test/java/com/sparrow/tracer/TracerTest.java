package com.sparrow.tracer;

import java.util.concurrent.CountDownLatch;

public class TracerTest {
    public static void main(String[] args) throws InterruptedException {
        Tracer tracer = TracerBuilder.startTracer();
        tracer.spanBuilder().name("l1").start();
        Span l2 = tracer.spanBuilder().asChild().name("l2").start();
        l2.finish();
        Span l3 = tracer.spanBuilder().asChild().name("l3").start();
        Span l31 = tracer.spanBuilder().asChild().name("l31").start();
        Span l32 = tracer.spanBuilder().asChild().name("l32").start();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new TracerTask(tracer, countDownLatch) {
            @Override
            public void task() {
                Span l321 = tracer.spanBuilder().asChild().name("l32-1").start();
                l321.finish();
            }
        }).start();
        new Thread(new TracerTask(tracer, countDownLatch) {
            @Override
            public void task() {
                Span l322 = tracer.spanBuilder().asChild().name("l32-2").start();
                l322.finish();
            }
        }).start();
        countDownLatch.await();
        l32.finish();
        l31.finish();
        l3.finish();
        Span l4 = tracer.spanBuilder().asChild().name("l4").start();
        l4.finish();
        tracer.root().finish();
        System.out.println(tracer.walking());
    }
}
