package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    static int b = 1;

    public static void main(String[] args) throws InterruptedException {

        final ReentrantLock lock = new ReentrantLock(false);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！" + 100 + "ms");
                //LockSupport.park();
                System.err.println(Thread.currentThread().getName() + "-" + Thread.currentThread().getState() + "-" + Thread.currentThread().isInterrupted());
                Thread.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "-" + Thread.currentThread().getState() + "-" + Thread.currentThread().isInterrupted());
                System.err.println(Thread.currentThread().getName() + "中断...");
            } finally {
                lock.unlock();
            }
        };

        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
//        thread1.setPriority(1);
        Thread thread2 = new Thread(runnable, "thread-2");
//        thread2.setPriority(1);
        Thread thread3 = new Thread(runnable, "thread-3");
//        thread3.setPriority(1);
        Thread thread4 = new Thread(runnable, "thread-4");
//        thread4.setPriority(1);
        Thread thread5 = new Thread(runnable, "thread-5");
//        thread5.setPriority(1);

        thread1.start();
        Thread.sleep(20);
        thread2.start();
        Thread.sleep(20);
        thread3.start();
        Thread.sleep(20);
        thread4.start();
        Thread.sleep(50);
        thread5.start();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println("QueueCount" + lock.getQueueLength());
                    System.out.println(thread1.getName() + "-" + thread1.getState() + "-" + thread1.isInterrupted());
                    System.out.println(thread2.getName() + "-" + thread2.getState() + "-" + thread2.isInterrupted());
                    System.out.println(thread3.getName() + "-" + thread3.getState() + "-" + thread3.isInterrupted());
                    System.out.println(thread4.getName() + "-" + thread4.getState() + "-" + thread4.isInterrupted());
                    System.out.println(thread5.getName() + "-" + thread5.getState() + "-" + thread5.isInterrupted());

                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if (b == 1) {
                    // thread1.interrupt();
                    //thread2.interrupt();
                    thread3.interrupt();
                    thread4.interrupt();
                    b = 0;
                    //}
                }
            }
        });
        monitor.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
