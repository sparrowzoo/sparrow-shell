package com.sparrow.facade.thread;

import java.util.concurrent.locks.LockSupport;

public class ThreadSupportTest {
    private static Integer lock = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                LockSupport.park();
                System.out.println("end.....");
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "中断...");
            }
        };

        Thread thread = new Thread(runnable, "test");
        thread.start();
        Thread.sleep(100);
        Thread thread2 = new Thread(runnable, "test2");
        thread2.start();
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
