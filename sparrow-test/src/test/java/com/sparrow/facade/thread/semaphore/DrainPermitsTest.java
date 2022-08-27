package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;

public class DrainPermitsTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        //将剩下的信号量一次性消耗光，并且返回所消耗的信号量
        System.out.println(semaphore.drainPermits());
        System.out.println(semaphore.availablePermits());
    }
}
