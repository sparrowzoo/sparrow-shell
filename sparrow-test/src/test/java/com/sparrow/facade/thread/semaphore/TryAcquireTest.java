package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;

public class TryAcquireTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        boolean t = semaphore.tryAcquire();
        //拿到成功，则真扣减
        System.out.println(t);
        t = semaphore.tryAcquire(10);
        //拿不到，则不扣减f
        System.out.println(t);
        System.out.println(semaphore.availablePermits());
    }
}
