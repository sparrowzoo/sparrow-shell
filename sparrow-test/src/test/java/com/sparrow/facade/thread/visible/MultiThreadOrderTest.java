package com.sparrow.facade.thread.visible;

public class MultiThreadOrderTest {
    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            final int fi = i;
            threads[i] = new Thread(new Runnable() {
                @Override public void run() {
                    System.out.println(fi);
                }
            });
            threads[i].start();
        }
    }
}
