package com.sparrow.facade.thread;

public class MultiThreadBlockTest {
    private static Object lock = new Object();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (lock){
                            Thread.sleep(1000000L);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
