package com.sparrow.facade.thread.count.down.latch;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class SyncTest {
    public static void main(String[] args) throws InterruptedException {
        Sync sync=new Sync(3);
        System.out.println(sync.getCount());
        sync.releaseShared(10);
        sync.releaseShared(10);
        System.out.println(sync.getCount());
        sync.acquireSharedInterruptibly(1);
        System.out.println(sync.getCount());

    }
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c-1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }
}
