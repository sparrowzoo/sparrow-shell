package com.sparrow.facade.thread.visible;

public class IoVisibleTest {
    static class ThreadDemo implements Runnable {
        public boolean flag = false;

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("set flag =" + flag);
        }

        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo, "t1").start();
        while (true) {
            System.out.println(threadDemo.getFlag());
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}