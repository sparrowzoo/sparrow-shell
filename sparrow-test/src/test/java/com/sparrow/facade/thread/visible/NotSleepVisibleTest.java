package com.sparrow.facade.thread.visible;

public class NotSleepVisibleTest {
    static class Inner implements Runnable {
        public boolean flag = false;
        @Override
        public void run() {
            //1. 先执行
            flag = true;
        }
        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Inner threadDemo = new Inner();
        new Thread(threadDemo, "t1").start();
        while (true) {
            //2. 因为这从来没有读过该值，所以读最新值
            //System.out.println("main flag " + threadDemo.flag);
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}