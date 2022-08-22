package com.sparrow.facade.thread.aqs.condition;

import java.util.concurrent.locks.LockSupport;

public class ParkTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                LockSupport.park();
                System.out.println("end...." + Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        //thread.interrupt();
    }
}
