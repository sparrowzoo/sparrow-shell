package com.sparrow.facade.thread.reentrant.lock.monitor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorExceptionTest {
    private static Lock mLock = new ReentrantLock();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int fi = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test(fi);
                }
            }).start();
        }
    }

    public static void test(int index) {
        try {
            mLock.lock();
            System.err.println(index + " run code" + index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
            System.err.println(index + " unlock:" + Thread.currentThread().getName());
        }
    }
}
