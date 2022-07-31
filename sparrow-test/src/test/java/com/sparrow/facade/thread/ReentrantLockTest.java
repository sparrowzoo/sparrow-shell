package com.sparrow.facade.thread;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {

        final ReentrantLock lock = new ReentrantLock(false);

        lock.lock();
        lock.lock();
        Runnable runnable = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                    lock.lockInterruptibly();
                    //long ms = new Random().nextInt(500);
                    System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！" + 100 + "ms");
                   while (true) {
    //                    if (Thread.currentThread().isInterrupted()) {
    //                        System.out.println(Thread.currentThread().getName() + " end....");
    //                        break;
    //                    }
                   }
                } catch (Exception e) {
                    System.err.println(Thread.currentThread().getName() + "中断...");
                } finally {
                    lock.unlock();
                }
        };


        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
//        thread1.setPriority(1);
      // Thread thread2 = new Thread(runnable, "thread-2");
//        thread2.setPriority(1);
//        Thread thread3 = new Thread(runnable, "thread-3");
//        thread3.setPriority(1);
//        Thread thread4 = new Thread(runnable, "thread-4");
//        thread4.setPriority(1);
//        Thread thread5 = new Thread(runnable, "thread-5");
//        thread5.setPriority(1);

        thread1.start();
        thread1.interrupt();
        //Thread.sleep(20);
        //thread2.start();
//        thread3.start();
//        thread4.start();
//        //Thread.sleep(50);
//        thread5.start();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //thread5.interrupt();
//        thread4.interrupt();
//        thread1.interrupt();
    }
}
