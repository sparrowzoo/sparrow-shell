package com.sparrow.tracer;

import com.sparrow.tracer.impl.TracerImpl;

import java.util.concurrent.CountDownLatch;

public abstract class TracerTask implements Runnable {
    public TracerTask(Tracer tracer, CountDownLatch countDownLatch) {
        this.tracer = tracer;
        this.cursor = tracer.cursor();
        this.countDownLatch = countDownLatch;
    }

    public TracerTask(Tracer tracer) {
        this(tracer, null);
    }

    private Tracer tracer;
    private Span cursor;
    private CountDownLatch countDownLatch;

    public abstract void task();

    @Override
    public void run() {
        ((TracerImpl) tracer).setCursor(this.cursor);
        try {
            this.task();
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }
}
