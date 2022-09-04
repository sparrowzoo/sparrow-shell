package com.sparrow.facade.thread.read.write.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        System.out.println("fetch write lock");
        new Thread(new Runnable() {
            @Override public void run() {
                writeLock.lock();
                System.out.println("other thread get lock");
            }
        }).start();
        Thread.sleep(5000);
        readLock.lock();
        System.out.println("fetch read lock");
        Thread.sleep(5000);
        writeLock.unlock();
    }
}
