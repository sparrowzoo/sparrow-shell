package com.sparrow.facade.thread;

public class ThreadInterruptTest {
    private static Integer lock = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备拿锁");
                System.out.println(Thread.currentThread().getName() + " 获取锁，执行业务逻辑！");
                synchronized (lock) {
                    System.out.println("entry " + Thread.currentThread().getName() + "-" + Thread.currentThread().getState());
                    //Thread.sleep(Integer.MAX_VALUE);
                    //LockSupport.park();
                    synchronized (lock) {
                        lock.wait();
                    }
                    System.out.println("exist " + Thread.currentThread().getName());
                }
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + "中断...");
            }
        };

        Thread thread = new Thread(runnable, "test");
        thread.start();
        Thread.sleep(100);
        Thread thread2 = new Thread(runnable, "test2");
        thread2.start();
        //thread.interrupt();
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
                    thread.interrupt();
                    //thread2.interrupt();
                }
            }
        });
        monitor.start();
    }
}
