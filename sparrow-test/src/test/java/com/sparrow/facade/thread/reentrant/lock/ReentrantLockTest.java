package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        final Lock lock = new ReentrantLock(false);
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！" + 100 + "ms");
                //LockSupport.park();
                Thread.sleep(Integer.MAX_VALUE);
                System.out.println(Thread.currentThread().getName() + "17" + Thread.currentThread().isInterrupted());
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " is_interrupted " + Thread.currentThread().getState() + "-" + Thread.currentThread().isInterrupted());
            } finally {
                lock.unlock();
            }
        };

        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
        Thread thread2 = new Thread(runnable, "thread-2");
        thread1.start();
        Thread.sleep(1000);
        thread2.start();
        Thread.sleep(2000);
        thread2.interrupt();
        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {

                while (true) {
                    synchronized (this) {
                        Thread thread = thread1;
                        System.out.println("MONITOR" + thread.getName() + "-" + thread.getState() + "-" + thread.isInterrupted());
                        System.out.println("MONITOR" + thread2.getName() + "-" + thread2.getState() + "-" + thread2.isInterrupted());
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitor.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
