package com.sparrow.jdk.threadlocal.local;

/**
 * @author by harry
 */
public class ThreadLocalTest extends Thread{
    private static MultiThreadLocalBusiness o=new MultiThreadLocalBusiness();

    public void run(){
        while (true) {
            o.setThreadId(Thread.currentThread().getId());
            o.business();
        }
    }

    public static void main(String[] args) {
        Thread thread=new ThreadLocalTest();
        thread.start();

        Thread thread2=new ThreadLocalTest();
        thread2.start();
    }
}