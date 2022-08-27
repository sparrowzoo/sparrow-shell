package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreTest1 {
    public static void main(String[] args) throws InterruptedException {
        //volatile int state
        //clh 队列
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        semaphore.acquire();
        System.out.println("2 获取信号量之后====" + semaphore.availablePermits());
        System.out.println("2 队列里的线程数====" + semaphore.getQueueLength());

        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println("monitor availablePermits" + semaphore.availablePermits());
                    System.out.println("monitor getQueueLength" + semaphore.getQueueLength());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitor.start();
        semaphore.acquire();

        System.out.println("获取信号量之后====" + semaphore.availablePermits());
        System.out.println("处理业务逻辑");
        semaphore.release(10);//在这释放是没有用的。为什么？
        System.out.println("释放信号量之后====" + semaphore.availablePermits());
    }
}
