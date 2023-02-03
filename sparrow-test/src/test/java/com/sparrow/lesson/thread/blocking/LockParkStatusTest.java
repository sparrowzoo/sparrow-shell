package com.sparrow.lesson.thread.blocking;

import java.util.concurrent.locks.LockSupport;

public class LockParkStatusTest {
    public static void main(String[] args) throws InterruptedException {
        Thread mainThread = Thread.currentThread();
        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println(mainThread.getName() +
                        "-state-" + mainThread.getState());
                    try {
                        Thread.sleep(1000);//
                        //System.out.println(Thread.currentThread().getName() +
                        //    "-state-" + Thread.currentThread().getState());
                    } catch (InterruptedException e) {

                    }
                }
            }
        }, "monitor-thread").start();
        LockSupport.park();//
    }
}
