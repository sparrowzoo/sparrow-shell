package com.sparrow.facade.thread.blocking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SyncBlockTest {
    private static final Object MONITOR = new Object();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        MONITOR.wait();
        Thread thread1 = new Thread(() -> {
            synchronized (SyncBlockTest.class) {
                System.out.println(String.format("[%s]-thread1 got monitor lock...", F.format(LocalDateTime.now())));
                try {
                    //MONITOR.wait();//报错 锁的对象和wait的对象不一致。
                    SyncBlockTest.class.wait();
                    System.out.println(String.format("[%s]-thread1 got monitor after wait.", F.format(LocalDateTime.now())));
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println(String.format("[%s]-thread1 exit waiting...", F.format(LocalDateTime.now())));
            }
        });
        thread1.start();
    }
}