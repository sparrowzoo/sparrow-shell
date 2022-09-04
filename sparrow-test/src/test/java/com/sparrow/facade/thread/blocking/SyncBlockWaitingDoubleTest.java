package com.sparrow.facade.thread.blocking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SyncBlockWaitingDoubleTest {

    private static final Object MONITOR = new Object();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Runnable runnable = () -> {
            synchronized (MONITOR) {
                System.out.println(String.format("[%s] [%s] got monitor lock...", F.format(LocalDateTime.now()), Thread.currentThread().getName()));
                try {
                    MONITOR.wait();
                    System.out.println(String.format("[%s] [%s] got monitor after wait.", F.format(LocalDateTime.now()), Thread.currentThread().getName()));
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println(String.format("[%s]-thread1 exit waiting...", F.format(LocalDateTime.now())));
            }
        };
        Thread thread1 = new Thread(runnable, "synchronized-thread");
        thread1.start();

        Thread thread2 = new Thread(runnable, "synchronized2-thread");
        thread2.start();

        while (true) {
            System.out.println(thread1.getName() + "- state " + thread1.getState());
            System.out.println(thread2.getName() + "- state " + thread2.getState());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
