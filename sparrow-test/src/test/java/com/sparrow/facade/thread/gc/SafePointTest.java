package com.sparrow.facade.thread.gc;

import java.util.concurrent.atomic.AtomicInteger;

public class SafePointTest {
    private static AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        long t = System.currentTimeMillis();
        Runnable runnable = new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 1000000000; i++) {
                    num.addAndGet(1);
                    //safepoint
                }
                //safepoint
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
        Thread.sleep(1000);
        System.out.println(System.currentTimeMillis() - t);
        System.out.println(num.get());
    }
}
