package com.sparrow.jdk.collections;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("upload");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
