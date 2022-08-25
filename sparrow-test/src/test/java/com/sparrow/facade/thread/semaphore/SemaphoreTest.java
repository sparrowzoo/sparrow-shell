package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire(1);
        semaphore.acquire(1);
        //另起线程，不阻塞当前线程，如果直接执行，则会阻塞
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    semaphore.acquire(1);
                    System.out.println("333");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        semaphore.release(2);
    }
}
