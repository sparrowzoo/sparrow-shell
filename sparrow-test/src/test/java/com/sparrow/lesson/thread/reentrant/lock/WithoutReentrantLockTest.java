package com.sparrow.lesson.thread.reentrant.lock;

public class WithoutReentrantLockTest {
    private static int stockCount = 1;
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            if (stockCount > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stockCount--;
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(runnable, "t1").start();
        }
        Thread.sleep(200);
        System.out.println(stockCount);
    }
}
