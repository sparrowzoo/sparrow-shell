package com.sparrow.facade.thread;

import java.util.concurrent.locks.LockSupport;

public class ParkTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                LockSupport.park();
               // Thread.currentThread().interrupt();
                System.out.println("end...."+Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        thread.interrupt();
    }
}
