package com.sparrow.cg;

import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService= new ThreadPoolExecutor(0, 2, 10L, TimeUnit.SECONDS, new SynchronousQueue<>(),
                new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                System.out.printf("搞不动了，扔给Q了");
            }
        });

        for(int i=0;i<100;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Future<String> future = executorService.submit(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                           return "hello"+System.currentTimeMillis();
                        }
                    });
                    try {
                       String result= future.get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
