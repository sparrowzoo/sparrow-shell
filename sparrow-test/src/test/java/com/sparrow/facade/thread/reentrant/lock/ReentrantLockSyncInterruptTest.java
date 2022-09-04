package com.sparrow.facade.thread.reentrant.lock;

import java.io.IOException;

public class ReentrantLockSyncInterruptTest {
    private static int stockCount = 1;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                synchronized (ReentrantLockSafeTest.class) {
                    if (stockCount > 0) {
                        try {
                            System.in.read();
                            //标准输入
                            //sleep locksupport.lock
                            //while(true){}
                            System.out.println("hello");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stockCount--;
                    }
                }
            }
        };
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable, "t" + i);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
        Thread.sleep(1000);
        System.out.println(stockCount);
    }
}
