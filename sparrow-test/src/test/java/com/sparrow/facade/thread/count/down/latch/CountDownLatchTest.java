package com.sparrow.facade.thread.count.down.latch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            countDownLatch.countDown();
        }
        countDownLatch.await();
    }
}
