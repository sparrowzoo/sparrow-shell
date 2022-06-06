package com.sparrow.jdk.threadlocal;

/**
 * @author by harry
 */
public class ThreadWithLocal extends Thread{
    public ThreadWithLocal(Long t) {
        this.t = t;
    }

    @Override public void run() {
        System.out.println(this.t);
    }

    //thread local
    private Long t;

    public static void main(String[] args) throws InterruptedException {
        ThreadWithLocal t=new ThreadWithLocal(System.nanoTime());
        t.start();
        Thread.sleep(5000L);
        //ThreadWithLocal t2=new ThreadWithLocal(System.nanoTime());
        t.start();
    }
}

