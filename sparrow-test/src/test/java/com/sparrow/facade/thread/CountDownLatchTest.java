package com.sparrow.facade.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(1);
        countDownLatch.countDown();
        countDownLatch.countDown();
        countDownLatch.await();
    }
}
