package com.sparrow.facade.thread.visible;

public class SynchronizedTest {
    static class ThreadDemo implements Runnable {
        private boolean flag = false;

        @Override
        public void run() {
            try {
                Thread.sleep(6);
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
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo, "t1").start();
        while (true) {
            synchronized (ThreadDemo.class) {
                if (threadDemo.getFlag()) {
                    System.out.println("end task");
                    break;
                }
            }
        }
    }
}