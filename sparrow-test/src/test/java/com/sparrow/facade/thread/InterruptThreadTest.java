package com.sparrow.facade.thread;

public class InterruptThreadTest {
    public static void main(String[] args) {
       Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
               while (true){
                   try {
                       System.out.println(Thread.currentThread().isInterrupted());
                       Thread.sleep(100);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(Thread.currentThread().isInterrupted());
               }
            }
        });
       thread.start();
       thread.interrupt();
    }
}
