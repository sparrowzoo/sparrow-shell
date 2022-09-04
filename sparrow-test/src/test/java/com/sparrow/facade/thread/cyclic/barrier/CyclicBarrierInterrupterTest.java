package com.sparrow.facade.thread.cyclic.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierInterrupterTest {
    public static void main(String[] args) throws InterruptedException {
        int cyclicBarriers = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(cyclicBarriers, new Runnable() {
            @Override public void run() {
                System.out.println(Thread.currentThread().getName() + "--ending....");
            }
        });

        Runnable runnable = new Runnable() {
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
        for (int i = 0; i < cyclicBarriers - 1; i++) {
            Thread t = new Thread(runnable, "t" + i);
            t.start();
        }
        Thread t2 = new Thread(runnable, "t" + cyclicBarriers);
        t2.start();
        t2.interrupt();
    }
}
