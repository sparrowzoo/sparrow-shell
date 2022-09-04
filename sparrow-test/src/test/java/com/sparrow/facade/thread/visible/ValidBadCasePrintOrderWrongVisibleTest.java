package com.sparrow.facade.thread.visible;

/**
 * 输出的随机性，导致输出结果不对，但是可见性是正确的
 */
public class ValidBadCasePrintOrderWrongVisibleTest {
    static class ThreadDemo implements Runnable {
        public volatile boolean flag = false;

        @Override
        public void run() {
            //System.out.println("set flag " + flag);
            /**
             *
             *  set flag true
             *  end task
             */
            flag = true;//1.set 值之后马上可见,并不是方法执行完才可见
            //4. 才打印
            /**
             * 可能是这个样子的F
             * end task
             * set flag true
             */
            System.out.println("set flag " + flag);
        }

        public boolean getFlag() {
            return flag;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo, "t1").start();

        while (true) {
            //boolean b = threadDemo.flag;
            //2.马上可见
            if (threadDemo.getFlag()) {
                //3. 先执行
                System.out.println("end task");
                break;
            }
        }
    }
}