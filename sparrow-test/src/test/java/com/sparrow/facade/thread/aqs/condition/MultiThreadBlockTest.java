package com.sparrow.facade.thread.aqs.condition;

public class MultiThreadBlockTest {
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[2];
        for (int i = 0; i < 2; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (lock) {
                            Thread.sleep(1000000L);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "thread_block" + i);
            threads[i].start();

        }
        Thread.sleep(2000);
        threads[1].interrupt();
    }
}
