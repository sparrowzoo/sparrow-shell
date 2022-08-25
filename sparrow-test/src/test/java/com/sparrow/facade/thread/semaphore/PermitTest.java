package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PermitTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(500);
        System.out.println(semaphore.getQueueLength() + "in queue");
        System.out.println(semaphore.availablePermits() + "available");
        semaphore.release(2);
        //Thread.sleep(3000);
        System.out.println(semaphore.availablePermits() + "permits");
    }
}
