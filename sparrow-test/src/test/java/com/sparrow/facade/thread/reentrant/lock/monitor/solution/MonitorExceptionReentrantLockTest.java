package com.sparrow.facade.thread.reentrant.lock.monitor.solution;

import java.util.concurrent.locks.ReentrantLock;

public class MonitorExceptionReentrantLockTest {
    private static ReentrantLock mLock = new ReentrantLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test(finalI);
                }
            }).start();
        }
    }

    public static void test(int index) {
        try {
            // 伪代码 开始
            System.err.println(index + " run code");
            // 伪代码 结束
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mLock.isHeldByCurrentThread()) {
                mLock.unlock();
            }
            System.err.println(index + " unlock:" + Thread.currentThread().getName());
        }
    }
}
