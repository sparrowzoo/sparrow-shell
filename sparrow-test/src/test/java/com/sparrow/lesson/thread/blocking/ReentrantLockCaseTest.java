package com.sparrow.lesson.thread.blocking;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCaseTest {
        public static void main(String[] args) throws InterruptedException {
            final ReentrantLock lock = new ReentrantLock();
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
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            };
            // 创建两个线程
            Thread thread1 = new Thread(runnable, "thread-1");
            Thread thread2 = new Thread(runnable, "thread-2");

            thread1.start();
            Thread.sleep(500);
            thread2.start();
            thread2.interrupt();
            //注意如果 换成thread2.interrupt();会是什么结果？为什么？
        }
}
