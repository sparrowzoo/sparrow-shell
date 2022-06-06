package com.sparrow.jdk.threadlocal;

/**
 * Created by harry on 2018/4/12.
 */
public class ThreadLocalGCLeak extends Thread {
    static ThreadLocal tl = new MyThreadLocal();

    public static class MyThreadLocal extends ThreadLocal {
        private byte[] threadObject = new byte[1024 * 1024 * 5];

        @Override
        public void finalize() {
            System.out.println(" threadlocal 1 MB finalized.");
        }
    }

    public static class MyBigObject {
        //占用内存的大对象
        private byte[] bigObject = new byte[1024 * 1024 * 50];
        @Override
        public void finalize() {
            System.out.println("50 MB Object finalized.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tl.set(new MyBigObject());
                //tl = null;//手动置为null，让gc回收
                System.gc();
                //System.out.println("Full GC 1" + tl.get());
                //保证线程不死，可以注释掉演示线程死亡的效果
//                while (true) {
//
//                }
            }
        });
        thread.setDaemon(false);
        thread.start();

        Thread.sleep(5000);
        System.gc();
        while (true) {
        }
    }
}
