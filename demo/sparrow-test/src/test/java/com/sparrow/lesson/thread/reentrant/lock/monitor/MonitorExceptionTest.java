package com.sparrow.lesson.thread.reentrant.lock.monitor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorExceptionTest {
    private static Lock mLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test(1);
            }
        });
        t.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test(1);
            }
        });
        t2.start();
        while (true) {
            System.out.println(t2.getState());
            Thread.sleep(1000);
        }
    }

    public static void test(int index) {
        try {
            mLock.tryLock();//阻塞
            Thread.sleep(5000);
            //mLock.tryLock();// 报错
            System.err.println(index + " run code" + index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();//为什么不允许释放s
            System.err.println(index + " unlock:" + Thread.currentThread().getName());
        }
    }
}
