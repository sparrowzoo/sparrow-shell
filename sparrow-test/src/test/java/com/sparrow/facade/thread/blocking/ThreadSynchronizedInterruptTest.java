package com.sparrow.facade.thread.blocking;

public class ThreadSynchronizedInterruptTest {
    private static Integer lock = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                synchronized (lock) {
                    System.out.println("entry " + Thread.currentThread().getName() + "-" + Thread.currentThread().getState());
                    Thread.sleep(Integer.MAX_VALUE);
                    //LockSupport.park();
                    System.out.println("exist " + Thread.currentThread().getName());
                }
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "中断...");
            }
        };

        Thread thread1 = new Thread(runnable, "test1");
        thread1.start();
        Thread.sleep(100);
        Thread thread2 = new Thread(runnable, "test2");
        thread2.start();
        thread2.interrupt();
        Thread monitor = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    System.out.println(thread1.getName() + "-" + thread1.getState());
                    System.out.println(thread2.getName() + "-" + thread2.getState());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        monitor.start();
    }
}