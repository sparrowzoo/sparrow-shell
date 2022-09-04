package com.sparrow.facade.thread.reentrant.lock;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInterruptTest {
    private static int stockCount = 1;
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    lock.lockInterruptibly();
                    if (stockCount > 0) {
                        try {
                            System.in.read();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stockCount--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread[] threads = new Thread[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable, "t" + i);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
        Thread.sleep(1000);
        System.out.println(stockCount);
    }
}
