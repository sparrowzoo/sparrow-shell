package com.sparrow.facade.thread.aqs.condition;

import java.util.concurrent.locks.LockSupport;

public class ParkTest {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        //sparrow-shell
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                LockSupport.park();
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                }
                System.out.println("end...." + Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        thread.interrupt();
    }
}
