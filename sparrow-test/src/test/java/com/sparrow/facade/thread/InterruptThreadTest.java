package com.sparrow.facade.thread;

public class InterruptThreadTest {
    public static void main(String[] args) {
       Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("hello");
                }
            }
        });
       thread.start();
       thread.interrupt();
    }
}
