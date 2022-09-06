package com.sparrow.facade.thread.visible;

public class SleepVisibleTest {
    static class ThreadDemo implements Runnable {
        public boolean flag = false;

        @Override
        public void run() {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            long t = System.currentTimeMillis();
//            while (true) {
//                long duration = System.currentTimeMillis() - t;
//                if (duration > 10) {
//                    break;
//                }
//            }
            while (true) {
                flag = true;
            }
        }

        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo, "t1").start();
        boolean b=threadDemo.getFlag();
        while (true) {
            //Thread.sleep(1000);
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}