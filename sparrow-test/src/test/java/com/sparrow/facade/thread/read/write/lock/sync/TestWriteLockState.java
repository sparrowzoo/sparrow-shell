package com.sparrow.facade.thread.read.write.lock.sync;

import com.sparrow.facade.thread.read.write.lock.OuterSync;

public class TestWriteLockState {
    public static void main(String[] args) throws InterruptedException {
        OuterSync.NonfairSync nonfairSync = new OuterSync.NonfairSync();
        OuterSync.WriteLock writeLock = new OuterSync.WriteLock(nonfairSync);
        int threadSize = 10;
        for (int i = 0; i < threadSize; i++) {
            //writeLock.lock();
            new Thread(writeLock::lock).start();
        }
        int state = 0;
        while (state != threadSize) {
            state = OuterSync.NonfairSync.exclusiveCount(nonfairSync.getCount());
            System.out.println((nonfairSync.getCount() & 0x0000FFFF) + "-" + state);
            Thread.sleep(1000);
        }
    }
}
