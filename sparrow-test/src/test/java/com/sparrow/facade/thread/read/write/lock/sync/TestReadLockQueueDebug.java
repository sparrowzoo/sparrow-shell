package com.sparrow.facade.thread.read.write.lock.sync;

import com.sparrow.facade.thread.read.write.lock.OuterSync;
import java.util.concurrent.locks.Condition;

public class TestReadLockQueueDebug {
    public static void main(String[] args) throws InterruptedException {
        OuterSync.NonfairSync nonfairSync = new OuterSync.NonfairSync();
        OuterSync.ReadLock readLock = new OuterSync.ReadLock(nonfairSync);
        OuterSync.WriteLock writeLock = new OuterSync.WriteLock(nonfairSync);

        writeLock.lock();
        int threadSize = 10;
        for (int i = 0; i < threadSize; i++) {
            new Thread(readLock::lock, "read-lock" + i).start();
        }
        System.out.println("debug ....");
    }
}
