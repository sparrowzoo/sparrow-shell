package com.sparrow.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("Hello World" + i);
                }
            }
        });
        future.get();
        executorService.shutdownNow();
        System.out.println("ending");
    }
}
