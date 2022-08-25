package com.sparrow.facade.thread.blocking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReenterSyncBlockingTest {

    private static final Object MONITOR = new Object();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        System.out.printf("[%s]-begin...%n", F.format(LocalDateTime.now()));
        Thread thread1 = new Thread(() -> {
            synchronized (MONITOR) {
                System.out.printf("[%s]-thread1 got monitor lock...%n", F.format(LocalDateTime.now()));
                try {
                    Thread.sleep(1000);
                    MONITOR.wait();
                    System.out.printf("[%s]-thread1 got monitor after wait.%n", F.format(LocalDateTime.now()));
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.printf("[%s]-thread1 exit waiting...%n", F.format(LocalDateTime.now()));
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (MONITOR) {
                System.out.printf("[%s]-thread2 got monitor lock...%n", F.format(LocalDateTime.now()));
                try {
                    MONITOR.notify();
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.printf("[%s]-thread2 releases monitor lock...%n", F.format(LocalDateTime.now()));
            }
        });
        thread1.start();
        thread2.start();
        Thread.sleep(3000);
        // 这里故意让主线程sleep 1500毫秒从而让thread2调用了Object#notify()并且尚未退出同步代码块，确保thread1调用了Object#wait()
        while (true) {
            Thread.sleep(1500);
            thread1.interrupt();
            System.out.println("Thread1- " + thread1.getState());
        }
    }
}