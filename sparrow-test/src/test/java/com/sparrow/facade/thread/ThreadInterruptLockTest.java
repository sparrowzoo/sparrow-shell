package com.sparrow.facade.thread;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadInterruptLockTest {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"-ending ...");
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "中断...");
            }
        };

        Thread thread = new Thread(runnable, "test");
        thread.start();
        Thread.sleep(100);
        Thread thread2 = new Thread(runnable, "test2");
        thread2.start();
        Thread.sleep(100);

        Thread thread3 = new Thread(runnable, "test3");
        thread3.start();
        //thread.interrupt();
        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println(thread.getName() + "-" + thread.getState());
                    System.out.println(thread2.getName() + "-" + thread2.getState());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    thread2.interrupt();
                }
            }
        });
        monitor.start();
    }
}
