package com.sparrow.facade.thread.reentrant.lock;

public class WithoutReentrantLockTest {
    private static int stockCount = 1;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                if (stockCount > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stockCount--;
                }
            }
        };
        new Thread(runnable, "t1").start();
        new Thread(runnable, "t2").start();
        Thread.sleep(1000);
        System.out.println(stockCount);
    }
}
