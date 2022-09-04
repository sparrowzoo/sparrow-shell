package com.sparrow.facade.thread.read.write.lock.sync;

import com.sparrow.facade.thread.read.write.lock.OuterSync;

public class TestReadLockState {
    public static void main(String[] args) throws InterruptedException {
        OuterSync.NonfairSync nonfairSync = new OuterSync.NonfairSync();
        OuterSync.ReadLock readLock = new OuterSync.ReadLock(nonfairSync);
        int threadSize = 10;
        for (int i = 0; i < threadSize; i++) {
            new Thread(readLock::lock).start();
        }
        int state = 0;
        while (state != threadSize) {
            state = OuterSync.NonfairSync.sharedCount(nonfairSync.getCount());
            System.out.println((nonfairSync.getCount() >>> 16) + "-" + state);
        }
    }
}
