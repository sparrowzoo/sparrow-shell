package com.sparrow.facade.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class MainTest {

    public static AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        long t=System.currentTimeMillis();
        Runnable runnable = () -> {
            for (long i = 0; i < 1000000000; i++) {
                num.getAndIncrement();
            }
            System.out.println(Thread.currentThread().getName() + " 执行结束"+(System.currentTimeMillis()-t)+"ms");
        };
        new Thread(runnable, "t1").start();
        new Thread(runnable, "t2").start();
        Thread.sleep(2000);
        System.out.println(" cost " +(System.currentTimeMillis()-t));
    }
}