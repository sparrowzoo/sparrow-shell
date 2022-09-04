package com.sparrow.facade.thread.count.down.latch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        System.out.println("count=" + countDownLatch.getCount());
        Runnable runnable = new Runnable() {
            @Override public void run() {
                System.out.println("do business");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        };
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            //new Thread(runnable).start();
            runnable.run();
            //System.out.println(countDownLatch.getCount());
        }
        countDownLatch.await();
        System.out.println("异步的时间" + (System.currentTimeMillis() - t));
    }
}
