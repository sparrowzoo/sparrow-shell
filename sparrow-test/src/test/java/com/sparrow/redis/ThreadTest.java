package com.sparrow.redis;

public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        try {
            Thread t = new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        int i = 1 / 0;
                        System.out.println("hello-1");
                    }catch (Exception e){
                        System.out.println("error inner");
                    }
                }
            });
            t.start();
        } catch (Throwable e) {
            System.out.println("error");
        }
    }
}
