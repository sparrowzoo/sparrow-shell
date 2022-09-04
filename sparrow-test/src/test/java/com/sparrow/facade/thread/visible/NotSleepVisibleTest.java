package com.sparrow.facade.thread.visible;

public class NotSleepVisibleTest {
    static class Inner implements Runnable {
        public Boolean flag = false;

        @Override
        public void run() {
            //1. 先执行
            flag = true;
            //System.out.println("set flag=true");
        }

        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Inner threadDemo = new Inner();
        new Thread(threadDemo, "t1").start();
        //Thread.sleep(5000);
        //CPU 调度的 GAP
        while (true) {
            //2. 因为这从来没有读过该值，所以读最新值
            System.out.println("main flag " + threadDemo.flag);
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}