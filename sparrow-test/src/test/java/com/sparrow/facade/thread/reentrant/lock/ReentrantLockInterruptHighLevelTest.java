package com.sparrow.facade.thread.reentrant.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * lockInterruptibly 响应中断后F为什么会报错？
 */
public class ReentrantLockInterruptHighLevelTest {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock();
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                //第二个线程进来会阻塞在这里
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                //第一个线程进来会在这里自旋
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + " end....");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        };
        // 创建两个线程
        Thread thread1 = new Thread(runnable, "thread-1");
        Thread thread2 = new Thread(runnable, "thread-2");

        thread1.start();

        Thread.sleep(500);//为什么停500ms
        //因为cpu调度存在gap，所以可能导致两个线程调度的时间不确定，影响我们程序的调试
        //join 也是可以的
        thread2.start();
        Thread.sleep(5000);
        thread2.interrupt();
        //注意如果 thread1 换成thread2.interrupt();会是什么结果？为什么？

        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println("MONITOR" + thread1.getName() + "-" + thread1.getState() + "-" + thread1.isInterrupted());
                    System.out.println("MONITOR" + thread2.getName() + "-" + thread2.getState() + "-" + thread2.isInterrupted());
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitor.start();
    }
}