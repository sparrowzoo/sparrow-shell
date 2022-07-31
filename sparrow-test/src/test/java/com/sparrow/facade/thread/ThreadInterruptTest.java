package com.sparrow.facade.thread;

public class ThreadInterruptTest {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + " successful exist....");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "中断...");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        thread.interrupt();
    }
}
