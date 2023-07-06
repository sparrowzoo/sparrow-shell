package com.sparrow.lesson.thread.aqs.condition;

import java.util.concurrent.locks.LockSupport;

public class ParkTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    LockSupport.park();
                    System.out.println(Thread.currentThread().isInterrupted());
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().isInterrupted());
                        break;
                    }
                    System.out.println("end...." + Thread.currentThread().isInterrupted());
                }
            }
        });
        thread.start();
        thread.interrupt();
    }
}
