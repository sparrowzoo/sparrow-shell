package com.sparrow.facade.thread;

import java.util.concurrent.Executors;

public class MultiThreadBlockTest {
    private static Object lock = new Object();

    public static void main(String[] args) {
        //Executors.newSingleThreadExecutor();
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (lock) {
                            lock.wait();
                            //Thread.sleep(1000000L);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "thread_block" + i).start();
        }
    }
}
