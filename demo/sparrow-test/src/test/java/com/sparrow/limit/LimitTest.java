package com.sparrow.limit;


import com.google.common.util.concurrent.RateLimiter;

public class LimitTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(0.1);
        while (true) {
            rateLimiter.acquire();
            //rateLimiter.tryAcquire(5,TimeUnit.SECONDS);
            System.out.println(System.currentTimeMillis()/1000);
        }
    }
}
