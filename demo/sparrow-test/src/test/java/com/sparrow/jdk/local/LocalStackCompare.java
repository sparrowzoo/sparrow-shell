package com.sparrow.jdk.local;


import java.util.concurrent.locks.ReentrantLock;

public class LocalStackCompare {
    private final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        LocalStackCompare compare = new LocalStackCompare();
        compare.stack();
    }

    private void stack() {
        final ReentrantLock lock = this.lock;
        this.lock.lock();
    }
}