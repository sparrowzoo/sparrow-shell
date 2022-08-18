package com.sparrow.facade.thread.visible;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    static class ThreadDemo implements Runnable {
        private boolean flag = false;

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("set flag:" + flag);
        }

        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest.ThreadDemo threadDemo = new SynchronizedTest.ThreadDemo();
        new Thread(threadDemo, "t1").start();
        Lock lock = new ReentrantLock();
        while (true) {
            lock.lock();
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
        lock.unlock();
    }
}
