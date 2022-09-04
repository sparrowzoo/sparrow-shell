package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.ReentrantLock;

public class FairNonFairTest {
    static ReentrantLock lock = new ReentrantLock(true);

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    lock.lockInterruptibly();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable, "t" + i);
            threads[i].start();
            Thread.sleep(100);
        }
        Thread.sleep(1000);
    }
}
