package com.sparrow.lesson.thread;

import com.sparrow.concurrent.SparrowThreadFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrentHashMapTest {
    private static Map<String, Object> map = new HashMap<>();
    private static CountDownLatch countDownLatch = new CountDownLatch(100);

    public static void main(String[] args) throws InterruptedException, IOException {
        for (int i = 0; i < 100; i++) {
            final int j = i;
            new Thread(new Runnable() {
                @Override public void run() {
                    map.put(j + "", new Object());
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println(map);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            8,
            1,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new SparrowThreadFactory.Builder().namingPattern("business-name-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.submit(new Runnable() {
            @Override public void run() {
                System.out.println(Thread.currentThread().getName());
                while (true) {

                }
            }
        });
        System.in.read();
    }
}
