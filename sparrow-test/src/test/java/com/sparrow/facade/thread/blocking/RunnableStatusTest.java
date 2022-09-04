package com.sparrow.facade.thread.blocking;

import java.util.concurrent.locks.LockSupport;

public class RunnableStatusTest {
    public static void main(String[] args) throws InterruptedException {
        Thread newThread = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                }
            }
        });
        newThread.start();
        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println(newThread.getName() + "-state-" + newThread.getState());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "monitor-thread").start();
        LockSupport.park();
    }
}
