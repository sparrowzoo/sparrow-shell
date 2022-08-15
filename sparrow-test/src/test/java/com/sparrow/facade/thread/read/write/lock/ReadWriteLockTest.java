package com.sparrow.facade.thread.read.write.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.readLock().lock();
       // readWriteLock.readLock().unlock();
    }
}
