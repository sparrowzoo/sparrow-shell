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

    public static void conditionWait() {
        try {
            lock.lock();
            System.out.println(String.format("[%s]-thread1 got monitor lock...", F.format(LocalDateTime.now())));
            condition.wait();
            System.out.println(String.format("[%s]-thread1 got monitor after wait.", F.format(LocalDateTime.now())));
        } catch (InterruptedException e) {
            //ignore
        } finally {
            lock.unlock();
        }
        System.out.println(String.format("[%s]-thread1 exit waiting...", F.format(LocalDateTime.now())));
    }

    public static void conditionAwait() {
        try {
            lock.lockInterruptibly();
            System.err.println(Thread.currentThread().getName() + " get lock");
            condition.await();
            System.out.println("ending...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(String.format("[%s]-begin...", F.format(LocalDateTime.now())));
        Thread thread1 = new Thread(ReentrantLockBlockingTest::conditionAwait, "thread1");
        // Thread thread1 = new Thread(ReentrantLockBlockingTest::conditionWait);

        Thread thread2 = new Thread(() -> {
            lock.lock();
            System.out.println(String.format("[%s]-thread2 got monitor lock...", F.format(LocalDateTime.now())));
            try {
                Thread.sleep(1000);
                condition.signal();
                Thread.sleep(1000);
                //condition.notify();//is error
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                //ignore
            } finally {
                lock.unlock();
            }
            System.out.println(String.format("[%s]-thread2 releases monitor lock...", F.format(LocalDateTime.now())));
        }, "thread2");
        thread1.start();
        Thread.sleep(1000);
        thread2.start();
        Thread.sleep(10000);
        while (true) {
            Thread.sleep(1500);
            System.err.println("Thread1 interrupt");
            thread1.interrupt();
            System.out.println("Thread1-" + thread1.getState() + "-interrupt- " + thread1.isInterrupted());
            System.out.println("Thread2-" + thread2.getState() + "-interrupt- " + thread2.isInterrupted());
        }
    }
}