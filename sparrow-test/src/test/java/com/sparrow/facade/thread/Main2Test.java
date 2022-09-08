package com.sparrow.facade.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Main2Test {

    public static AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            num.getAndIncrement();
        }
        System.out.println(System.currentTimeMillis() - t);

        t = System.currentTimeMillis();
        for (long i = 0; i < 1000000000; i++) {
            num.getAndIncrement();
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}