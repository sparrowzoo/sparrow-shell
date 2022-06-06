package com.sparrow.limit;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.TimeUnit;

public class LimitTest2 {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1,5,TimeUnit.SECONDS);
        System.out.println(rateLimiter.getRate());
        while (true) {
            System.out.println("get token "+ rateLimiter.acquire()+"s");
        }
    }
}
