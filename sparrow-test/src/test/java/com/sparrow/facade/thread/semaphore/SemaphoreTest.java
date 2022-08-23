package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire(1);
        semaphore.acquire(1);

        //semaphore.acquire(1);
        semaphore.release(2);
        //System.out.println(semaphore.tryAcquire());
        //semaphore.tryAcquire(10);
        System.out.println(semaphore.availablePermits());
        //将剩下的信号量一次性消耗光，并且返回所消耗的信号量
        //System.out.println(semaphore.drainPermits());
//        new Thread(new Runnable() {
//            @Override public void run() {
//                try {
//                    semaphore.tryAcquire(1000, TimeUnit.DAYS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        Thread.sleep(500);
//        System.out.println(semaphore.getQueueLength());
//        System.out.println(semaphore.availablePermits());
//        semaphore.release(2);
//        semaphore.release(-1);
//        System.out.println(semaphore.availablePermits());
    }
}
