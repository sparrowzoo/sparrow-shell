package com.sparrow.facade.thread.interrupter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockBlockingTest {

    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void conditionSignal() {
        lock.lock();
        System.out.printf("[%s]-[%s] got monitor lock...%n", F.format(LocalDateTime.now()),Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            condition.signal();
            //Thread.sleep(20000);
            //Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            //ignore
        } finally {
            lock.unlock();
        }
        System.out.printf("[%s]-thread2 releases monitor lock...%n", F.format(LocalDateTime.now()));
    }

    public static void conditionAwait() {
        try {
            lock.lockInterruptibly();
            System.err.println(Thread.currentThread().getName() + " get lock");
            condition.await();
            System.out.println(Thread.currentThread().getName() + " signed");
            System.out.println("ending...");
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.printf("[%s]-begin...%n", F.format(LocalDateTime.now()));
        Thread thread1 = new Thread(ReentrantLockBlockingTest::conditionAwait, "waiting-thread");
        Thread thread2 = new Thread(ReentrantLockBlockingTest::conditionSignal, "signal-thread");
        thread1.start();
        Thread.sleep(1000);
        thread2.start();
        thread1.interrupt();
        while (true) {
            Thread.sleep(1500);
            System.err.println(thread1.getName()+" interrupt");
            System.out.println(thread1.getName() + "-" + thread1.getState() + "-interrupt- " + thread1.isInterrupted());
            System.out.println(thread2.getName() + "-" + thread2.getState() + "-interrupt- " + thread2.isInterrupted());
        }
    }
}