package com.sparrow.facade.thread.visible;

import java.util.concurrent.TimeUnit;

public class SleepTest {
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
        while (true) {
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
            //Thread.sleep(100);
            System.out.println(Thread.currentThread().getName());
        }
    }
}