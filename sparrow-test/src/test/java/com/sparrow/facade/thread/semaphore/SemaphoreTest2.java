package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreTest2 {
    public static void main(String[] args) throws InterruptedException {
        //volatile int state
        //clh 队列
        Semaphore semaphore = new Semaphore(2);
        Runnable entryRunnable = new Runnable() {
            @Override public void run() {
                try {
                    semaphore.acquire();
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " 获取信号量之后= " + semaphore.availablePermits()+",队列里的线程数=" + semaphore.getQueueLength());
                } catch (Exception ignore) {
                } finally {
                    semaphore.release();
                }
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(entryRunnable).start();
        }

        System.out.println("获取信号量之后 availablePermits=" + semaphore.availablePermits() + " getQueueLength=" + semaphore.getQueueLength());
        Thread.sleep(6000);
        System.out.println("获取信号量6s之后 availablePermits=" + semaphore.availablePermits() + "  getQueueLength=" + semaphore.getQueueLength());
//
        semaphore.release(-10);//在这释放是没有用的。为什么？
        System.out.println("释放信号量之后====" + semaphore.availablePermits());
    }
}
