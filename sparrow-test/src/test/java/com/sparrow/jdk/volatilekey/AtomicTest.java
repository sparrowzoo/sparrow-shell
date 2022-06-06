package com.sparrow.jdk.volatilekey;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by harry on 2018/4/8.
 */
public class AtomicTest extends Thread {

    private static CountDownLatch waiter = new CountDownLatch(2);

    //private static volatile int count = 0;

    private static volatile AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        for (int i = 0; i < 100000000; i++) {
            count.getAndAdd(1);
        }
        waiter.countDown();
    }

    public static void main(String[] args) {
        AtomicTest tt = new AtomicTest();
        tt.start();
        AtomicTest t2 = new AtomicTest();
        t2.start();
        try {
            waiter.await();
            System.out.println(AtomicTest.count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
