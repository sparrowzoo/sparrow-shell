package com.sparrow.redis;

import java.time.Duration;

public class TimeTest {
    public static void main(String[] args) {
        long t=System.nanoTime();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long duration=System.nanoTime()-t;
        System.out.println(Duration.ofNanos(duration).toMillis());
    }
}
