package com.sparrow.lesson.thread.reentrant.lock;

public class ReentrantLockSyncInterruptTest {
    private static int stockCount = 1;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                synchronized (ReentrantLockSafeTest.class) {
                    try {
                        //标准输入
                        System.in.read();
                        /**
                         *  if any thread has interrupted the current thread. The
                         *                <i>interrupted status</i> of the current thread is
                         *               cleared when this exception is thrown.
                         */
                        //Thread.sleep(100);//中断异常 清除中断标记
                        //LockSupport.park();//不抛异常 直接响应中段
                        //System.out.println(Thread.currentThread().getName() + "'s interrupt status is " + Thread.currentThread().isInterrupted());
                        // 它不会抛出中断异常，而是从park方法直接返回，不影响线程的继续执行
//                        while (true) {
//                        }
                        //System.out.println("hello");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(Thread.currentThread().getName() + "'s interrupt status is " + Thread.currentThread().isInterrupted());
                    }
                }
            }
        };
        Thread[] threads = new Thread[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable, "thread-name" + i);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            thread.interrupt();
            System.out.println("thread interrupt status is " + thread.isInterrupted() + ",thread name is " + thread.getName());
        }
        Thread.sleep(1000);
    }
}
