package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        final Lock lock = new ReentrantLock(false);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！" + 100 + "ms");
                //LockSupport.park();
                Thread.sleep(Integer.MAX_VALUE);
                System.out.println(Thread.currentThread().getName()+"17"+Thread.currentThread().isInterrupted());
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " is_interrupted " + Thread.currentThread().getState() + "-" + Thread.currentThread().isInterrupted());
                while (true) {
                   // System.out.println("TRY-----"+Thread.currentThread().getName() + "-" + Thread.currentThread().getState() + "-" + Thread.currentThread().isInterrupted());
                }
            } finally {
                lock.unlock();
            }
        };

        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
//        thread1.setPriority(1);
        Thread thread2 = new Thread(runnable, "thread-2");
//        thread2.setPriority(1);
        //Thread thread3 = new Thread(runnable, "thread-3");
//        thread3.setPriority(1);
        //Thread thread4 = new Thread(runnable, "thread-4");
//        thread4.setPriority(1);
        //Thread thread5 = new Thread(runnable, "thread-5");
//        thread5.setPriority(1);

        thread1.start();
        Thread.sleep(1000);
        //Thread.sleep(20);
        thread2.start();
        //Thread.sleep(20);
        //thread3.start();
        //Thread.sleep(20);
        //thread4.start();
        //Thread.sleep(50);
        //thread5.start();
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Thread.sleep(5000);
            thread1.interrupt();

        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {

                while (true) {
                    synchronized (this) {
                        Thread thread = thread1;
                        System.out.println("MONITOR" + thread.getName() + "-" + thread.getState() + "-" + thread.isInterrupted());
                        System.out.println("MONITOR" + thread2.getName() + "-" + thread2.getState() + "-" + thread2.isInterrupted());
//                    System.out.println(thread3.getName() + "-" + thread3.getState() + "-" + thread3.isInterrupted());
//                    System.out.println(thread4.getName() + "-" + thread4.getState() + "-" + thread4.isInterrupted());
//                    System.out.println(thread5.getName() + "-" + thread5.getState() + "-" + thread5.isInterrupted());
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //thread2.interrupt();
//                        thread3.interrupt();
//                        thread4.interrupt()
                    //}
                }
            }
        });
        monitor.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
