package com.sparrow.lesson.thread.aqs.condition;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    static ReentrantLock lock = new ReentrantLock();
    /**
     * 之所以有condition的等待队列
     * <p>
     * 1.是为了更方便的实现线程的等待/通知；
     * <p>
     * 2. 避免了在不符合条件的前提下，线程无用的获取同步状态，节省资源
     */
    static Condition empty = lock.newCondition();
    static AtomicInteger count = new AtomicInteger();


    public static void main(String[] args) throws InterruptedException {
        ConditionTest conditionTest = new ConditionTest();
        int threadCount = 10;
        Thread takes[] = new Thread[threadCount];
        Thread puts[] = new Thread[threadCount];
        for (int i = 0; i < takes.length; i++) {
            Thread thread = new Thread(conditionTest::take, "taking-thread " + i);
            thread.start();
            takes[i] = thread;
        }
        for (int i = 0; i < takes.length; i++) {
            Thread thread = new Thread(conditionTest::put, "putting-thread" + i);
            thread.start();
            puts[i] = thread;
        }
    }

    public Integer take() {
        try {
            lock.lockInterruptibly();
            if (count.get() < 5) {
                empty.await();
            }
            count.addAndGet(-5);
            System.out.println(" taking-" + 5 + Thread.currentThread().getName());
            return 0;
        } catch (Exception e) {
            return 0;
        } finally {
            lock.unlock();
        }
    }

    public void put() {
        try {
            lock.lockInterruptibly();
            count.getAndIncrement();
            System.out.println(Thread.currentThread().getName() + "- put");

            if (count.get() % 5 == 0) {
                System.out.println(Thread.currentThread().getName() + "-" + count.get());
                empty.signal();
            }
            Thread.sleep(100);
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }
}
