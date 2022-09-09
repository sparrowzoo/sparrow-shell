package com.sparrow.facade.thread.cyclic.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier和CountDownLatch区别
 * <p>
 * CountDownLatch的await()线程会等待计数器减为0，
 * <p>
 * 而执行CyclicBarrier的await()方法会使线程进入阻塞等待其他线程到达障点。【--count;】并阻塞
 * <p>
 * CountDownLatch计数器不能重置，CyclicBarrier可以重置循环利用。
 * <p>
 * CountDownLatch是基于AQS的共享模式实现的，
 * <p>
 * CyclicBarrier是基于ReentrantLock和Condition实现的。
 * <p>
 * <p>
 * CountDownLatch不会让子线程进入阻塞，
 * <p>
 * CyclicBarrier会使所有子线程进入阻塞。
 */
public class CyclicBarrierTest {
    public  static void main(String[] args) throws InterruptedException {
        //parties =3
        //count =3
        /**
         *  this.parties = parties;
         *  this.count = parties;
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override public void run() {
                System.out.println(Thread.currentThread().getName() + "ending....");
            }
        });
        Runnable  runnable = new Runnable() {
            @Override public void run() {
                try {
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    System.err.println(Thread.currentThread().getName() + " interrupted");
                } catch (BrokenBarrierException e) {
                    System.err.println(Thread.currentThread().getName() + " broken");
                }
            }
        };
        //count =3-1=2
        new Thread(runnable, "t1").start();

        Thread.sleep(1000);
        //parties-count=3-2=1
        System.out.println(cyclicBarrier.getNumberWaiting());
        cyclicBarrier.reset();

        System.out.println("is broken " + cyclicBarrier.isBroken());
        //parties-count=3-3=0
        System.out.println(cyclicBarrier.getNumberWaiting());

        new Thread(runnable, "t2").start();
        new Thread(runnable, "t3").start();
        new Thread(runnable, "t4").start();
        System.out.println("2-3-4");
        Thread.sleep(5000);
        new Thread(runnable, "t5").start();
        new Thread(runnable, "t6").start();
        new Thread(runnable, "t7").start();
        System.out.println("5-6-7");

        Thread.sleep(5000);
        while (cyclicBarrier.getNumberWaiting() > 0) {
            System.out.println("getNumberWaiting---->" + cyclicBarrier.getNumberWaiting());
        }
    }
}
