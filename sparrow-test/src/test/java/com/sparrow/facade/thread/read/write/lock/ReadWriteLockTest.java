package com.sparrow.facade.thread.read.write.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

        //new Thread(readLock::lock).start();
        //new Thread(readLock::lock).start();
        //readLock.unlock();

        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        System.out.println("fetch write lock");
        writeLock.lock();
        writeLock.unlock();
    }
}
