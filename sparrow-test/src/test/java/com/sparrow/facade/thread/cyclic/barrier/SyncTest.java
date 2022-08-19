package com.sparrow.facade.thread.cyclic.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTest {
    public static void main(String[] args) throws InterruptedException {
        SyncTest syncTest=new SyncTest(new Runnable() {
            @Override public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        },3);

        Runnable runnable=new Runnable() {
            @Override public void run() {
                try {
                    syncTest.await();
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    System.err.println(Thread.currentThread().getName()+"interrupted");
                } catch (BrokenBarrierException e) {
                    System.err.println(Thread.currentThread().getName()+"broken");
                }
            }
        };
        System.out.println("thread-count:"+syncTest.getCount());
        new Thread(runnable,"t1").start();
        Thread.sleep(1000);
        System.out.println("thread-count:"+syncTest.getCount());
        Thread.sleep(1000);
        new Thread(runnable,"t2").start();
        new Thread(runnable,"t3").start();
        new Thread(runnable,"t4").start();
        new Thread(runnable,"t5").start();
        System.out.println("thread-count:"+syncTest.getCount());
    }
    public SyncTest(Runnable barrierCommand,Integer parties) {
        this.barrierCommand = barrierCommand;
        this.parties=parties;
        this.count = parties;
    }

    /**
     * Each use of the barrier is represented as a generation instance.
     * The generation changes whenever the barrier is tripped, or
     * is reset. There can be many generations associated with threads
     * using the barrier - due to the non-deterministic way the lock
     * may be allocated to waiting threads - but only one of these
     * can be active at a time (the one to which {@code count} applies)
     * and all the rest are either broken or tripped.
     * There need not be an active generation if there has been a break
     * but no subsequent reset.
     */
    private static class Generation {
        boolean broken = false;
    }

    /** The lock for guarding barrier entry */
    private final ReentrantLock lock = new ReentrantLock();
    /** Condition to wait on until tripped */
    private final Condition trip = lock.newCondition();
    /** The number of parties */
    private final int parties;
    /* The command to run when tripped */
    private final Runnable barrierCommand;
    /** The current generation */
    private Generation generation = new Generation();

    /**
     * Number of parties still waiting. Counts down from parties to 0
     * on each generation.  It is reset to parties on each new
     * generation or when broken.
     */
    private int count;

    public int getCount() {
        return count;
    }

    /**
     * Updates state on barrier trip and wakes up everyone.
     * Called only while holding lock.
     */
    private void nextGeneration() {
        // signal completion of last generation
        trip.signalAll();
        // set up next generation
        count = parties;
        generation = new Generation();
    }

    /**
     * Sets current barrier generation as broken and wakes up everyone.
     * Called only while holding lock.
     */
    private void breakBarrier() {
        generation.broken = true;
        count = parties;
        trip.signalAll();
    }

    /**
     * Main barrier code, covering the various policies.
     */
    private int dowait(boolean timed, long nanos)
        throws InterruptedException, BrokenBarrierException,
        TimeoutException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Generation g = generation;

            if (g.broken)
                throw new BrokenBarrierException();

            if (Thread.interrupted()) {
                breakBarrier();
                throw new InterruptedException();
            }

            int index = --count;// volatile
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null)
                        command.run();
                    ranAction = true;
                    nextGeneration();
                    return 0;
                } finally {
                    if (!ranAction)
                        breakBarrier();
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            for (;;) {
                try {
                    if (!timed)
                        trip.await();
                    else if (nanos > 0L)
                        nanos = trip.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken)
                    throw new BrokenBarrierException();

                if (g != generation)
                    return index;

                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int getParties() {
        return parties;
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }

    public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
        BrokenBarrierException,
        TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }


    public boolean isBroken() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return generation.broken;
        } finally {
            lock.unlock();
        }
    }


    public void reset() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            breakBarrier();   // break the current generation
            nextGeneration(); // start a new generation
        } finally {
            lock.unlock();
        }
    }


    public int getNumberWaiting() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return parties - count;
        } finally {
            lock.unlock();
        }
    }

    public static class ThreadInterruptLockTest {
        private static ReentrantLock lock = new ReentrantLock();

        public static void main(String[] args) throws InterruptedException {
            Runnable runnable = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                    System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName()+"-ending ...");
                } catch (Exception e) {
                    System.err.println(Thread.currentThread().getName() + "中断...");
                }
            };

            Thread thread = new Thread(runnable, "test1");
            thread.start();
            Thread.sleep(100);
            Thread thread2 = new Thread(runnable, "test2");
            thread2.start();
            Thread.sleep(100);

            Thread thread3 = new Thread(runnable, "test3");
            thread3.start();

            Thread monitor = new Thread(new Runnable() {
                @Override public void run() {
                    while (true) {
                        System.out.println(thread.getName() + "-" + thread.getState());
                        System.out.println(thread2.getName() + "-" + thread2.getState());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        thread2.interrupt();
                    }
                }
            });
            monitor.start();
        }
    }
}
