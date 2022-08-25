package com.sparrow.facade.thread.interrupter;

public class InterruptThreadWhileTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().isInterrupted());
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("do something for business");
                        System.out.println("close database connection");
                        System.out.println("release io connection");
                        break;
                    }
                }
            }
        });
        thread.start();
        thread.interrupt();
    }
}
