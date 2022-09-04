package com.sparrow.facade.thread.count.down.latch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchCountTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        System.out.println("count=" + countDownLatch.getCount());
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {

            countDownLatch.countDown();
            System.out.println(countDownLatch.getCount());
        }
        countDownLatch.await();
        System.out.println("异步的时间" + (System.currentTimeMillis() - t));
    }
}
