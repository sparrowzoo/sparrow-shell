package com.sparrow.facade.thread.blocking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReenterSyncBlockingTest {

    private static final Object MONITOR = new Object();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        System.out.println(String.format("[%s]-begin...", F.format(LocalDateTime.now())));
        Thread thread1 = new Thread(() -> {
            synchronized (MONITOR) {
                System.out.println(String.format("[%s]-thread1 got monitor lock...", F.format(LocalDateTime.now())));
                try {
                    MONITOR.wait();
                    System.out.println(String.format("[%s]-thread1 got monitor after wait.", F.format(LocalDateTime.now())));
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println(String.format("[%s]-thread1 exit waiting...", F.format(LocalDateTime.now())));
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (MONITOR) {
                System.out.println(String.format("[%s]-thread2 got monitor lock...", F.format(LocalDateTime.now())));
                try {
                    MONITOR.notify();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println(String.format("[%s]-thread2 releases monitor lock...", F.format(LocalDateTime.now())));
            }
        });
        thread1.start();
        Thread.sleep(1000);
        thread2.start();
        for (int i = 0; i < 5; i++) {
            System.out.println("Thread1-" + thread1.getState() + "---Thread2-" + thread2.getState());
            Thread.sleep(1000);
        }
    }
}