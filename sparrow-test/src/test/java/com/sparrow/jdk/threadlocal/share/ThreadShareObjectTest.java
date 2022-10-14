package com.sparrow.jdk.threadlocal.share;

/**
 * @author by harry
 */
public class ThreadShareObjectTest extends Thread{
    private static MultiThreadShareBusiness o=new MultiThreadShareBusiness();

    public void run(){
        while (true) {
            o.setThreadId(Thread.currentThread().getId());
            o.business();
        }
    }

    public static void main(String[] args) {
        Thread thread=new ThreadShareObjectTest();
        thread.start();

        Thread thread2=new ThreadShareObjectTest();
        thread2.start();
    }
}