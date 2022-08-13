package com.sparrow.facade.thread.aqs.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {
    final ReentrantLock lock = new ReentrantLock();
    Condition empty = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ConditionTest conditionTest = new ConditionTest();
        Thread thread = new Thread(conditionTest::take);
        thread.start();
        Thread thread1 = new Thread(conditionTest::put);
        thread1.start();
        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println(thread.getName() + "-" + thread.getState());
                    System.out.println(thread1.getName() + "-" + thread1.getState());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    thread.interrupt();
                    //thread1.interrupt();
                }
            }
        });
        monitor.start();
    }

    public Integer take() {
        try {
            lock.lockInterruptibly();
            try {
                Thread.sleep(5000);
            } catch (Throwable e) {
            }
            empty.await();
            System.out.println("ending...");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            lock.unlock();
        }
    }

    public void put() {
        try {
            lock.lockInterruptibly();
            empty.signal();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}