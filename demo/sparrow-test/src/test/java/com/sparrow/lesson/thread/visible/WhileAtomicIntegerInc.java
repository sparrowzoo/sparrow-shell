package com.sparrow.lesson.thread.visible;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Under the new memory model, it is still true that volatile variables cannot be reordered with each other. The
 * difference is that it is now no longer so easy to reorder normal field accesses around them. Writing to a volatile
 * field has the same memory effect as a monitor release, and reading from a volatile field has the same memory effect
 * as a monitor acquire. In effect, because the new memory model places stricter constraints on reordering of volatile
 * field accesses with other field accesses, volatile or not, anything that was visible to thread A when it writes to
 * volatile field f becomes visible to thread B when it reads f.
 */
public class WhileAtomicIntegerInc {
    private AtomicInteger x = new AtomicInteger(0);
    private AtomicInteger y = new AtomicInteger(0);
    static int count = 10000;
    ReentrantLock lock = new ReentrantLock();
    static CountDownLatch latch = new CountDownLatch(count);

    public static void main(String[] args) throws InterruptedException {
        WhileAtomicIntegerInc v = new WhileAtomicIntegerInc();
        for (int i = 0; i < count; i++) {
            new Thread(v::inc).start();
        }
        latch.await();
        System.out.println("y=" + v.y.get());
        System.out.println("x=" + v.x.get());
    }

    public void inc() {
        //lock.lock();
        y.addAndGet(1);
        x.addAndGet(1);
        latch.countDown();
        //lock.unlock();
    }
}
