package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockSafeTest {
    private static int stockCount = 1;
    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                reentrantLock.lock();
                try {
                    if (stockCount > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        stockCount--;
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        };
        for (int i = 0; i < 100; i++) {
            new Thread(runnable, "t1").start();
        }
        Thread.sleep(1000);
        System.out.println(stockCount);
    }
}
