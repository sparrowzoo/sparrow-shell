package com.sparrow.facade.thread.visible;

/**
 * 输出的随机性，导致输出结果不对，但是可见性是正确的
 */
public class BadCasePrintOrderWrongVisibleTest {
    static class ThreadDemo implements Runnable {
        public volatile boolean flag = false;

        @Override
        public void run() {
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
        //Thread.sleep(1000);  不允许sleep
        //CPU 调度的 GAP
        while (true) {
            System.out.println(threadDemo.flag);
            //boolean b = threadDemo.flag;
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}