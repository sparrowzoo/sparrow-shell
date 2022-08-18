package com.sparrow.facade.thread.cyclic.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier和CountDownLatch区别
 * CountDownLatch的await()线程会等待计数器减为0，而执行CyclicBarrier的await()方法会使线程进入阻塞等待其他线程到达障点。 【--count;】并阻塞
 * CountDownLatch计数器不能重置，CyclicBarrier可以重置循环利用。
 * CountDownLatch是基于AQS的共享模式实现的，CyclicBarrier是基于ReentrantLock和Condition实现的。
 * CountDownLatch不会让子线程进入阻塞，CyclicBarrier会使所有子线程进入阻塞。
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(3, new Runnable() {
            @Override public void run() {
                System.out.println("ending....");
            }
        });

        Runnable runnable=new Runnable() {
            @Override public void run() {
                try {
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    System.err.println(Thread.currentThread().getName()+"interrupted");
                } catch (BrokenBarrierException e) {
                    System.err.println(Thread.currentThread().getName()+"broken");
                }
            }
        };
        new Thread(runnable,"t1").start();
        Thread.sleep(1000);
        System.out.println(cyclicBarrier.getNumberWaiting());
        cyclicBarrier.reset();
        System.out.println(cyclicBarrier.getNumberWaiting());

        new Thread(runnable,"t2").start();
        Thread.sleep(2000);
        new Thread(runnable,"t3").start();
        Thread t= new Thread(runnable,"t4");
        t.start();
        new Thread(runnable,"t5").start();
        new Thread(runnable,"t6").start();
        new Thread(runnable,"t7").start();
        while (cyclicBarrier.getNumberWaiting()>0){
            System.out.println("getNumberWaiting---->"+cyclicBarrier.getNumberWaiting());
        }
        System.out.println("start");
    }
}
