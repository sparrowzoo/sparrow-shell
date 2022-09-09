package com.sparrow.facade.thread.visible;

/**
 * Under the new memory model, it is still true that volatile variables cannot be reordered with each other. The
 * difference is that it is now no longer so easy to reorder normal field accesses around them. Writing to a volatile
 * field has the same memory effect as a monitor release, and reading from a volatile field has the same memory effect
 * as a monitor acquire.
 * <p>
 * In effect, because the new memory model places stricter constraints on reordering of volatile field accesses with
 * other field accesses, volatile or not, anything that was visible to thread A when it writes to volatile field f
 * becomes visible to thread B when it reads f.
 */
public class NotVisibleTest {
    static class ThreadDemo implements Runnable {
        public volatile boolean flag = false;
        private long[] longs=new long[640];
        private boolean flag2 = false;

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //2. 设置新true,但其他线程暂不可见
            flag = true;
            flag2 = true;
            System.out.println("set flag =" + flag);
        }

        public boolean getFlag() {
            return flag2;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        new Thread(threadDemo, "t1").start();
        //Thread.sleep(1000);  不允许sleep
        //CPU 调度的 GAP
        while (true) {
            //System.out.println(threadDemo.flag);
            boolean b = threadDemo.flag;
            //1.先取老的flag=false;
            if (threadDemo.getFlag()) {
                System.out.println("end task");
                break;
            }
        }
    }
}