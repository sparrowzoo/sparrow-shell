package com.sparrow.facade.thread;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock(true);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + " end....");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + "中断...");
            } finally {
                lock.unlock();
            }
        };
        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
        Thread thread2 = new Thread(runnable, "thread-2");
        Thread thread3 = new Thread(runnable, "thread-3");
        Thread thread4 = new Thread(runnable, "thread-4");
        Thread thread5 = new Thread(runnable, "thread-5");
        Thread thread6 = new Thread(runnable, "thread-6");

        thread1.start();
        Thread.sleep(500);
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        thread5.interrupt();
        thread4.interrupt();
        while (true) {
        }
    }
}
