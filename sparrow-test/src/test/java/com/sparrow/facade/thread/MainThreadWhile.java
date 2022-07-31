package com.sparrow.facade.thread;

public class MainThreadWhile {
    public static void main(String[] args) throws InterruptedException {
        boolean b = true;
        Thread.sleep(3000);
        while (b) {

        }
        System.out.println("hello");
    }
}
