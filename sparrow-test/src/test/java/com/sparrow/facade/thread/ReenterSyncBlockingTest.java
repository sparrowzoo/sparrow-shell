package com.sparrow.facade.thread;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 线程2调用Object#notify()后睡眠2000毫秒再退出同步代码块，释放监视器锁。
 * <p>
 * 线程1只睡眠了1000毫秒就调用了Object#wait()，此时它已经释放了监视器锁，
 * <p>
 * 所以线程2成功进入同步块，线程1处于API注释中所述的reenter a synchronized block/method的状态。
 * <p>
 * <p>
 * 主线程睡眠1500毫秒刚好可以命中线程1处于reenter状态并且打印其线程状态，刚好就是BLOCKED状态。
 */
public class ReenterSyncBlockingTest {

    private static final Object MONITOR = new Object();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        System.out.println(String.format("[%s]-begin...", F.format(LocalDateTime.now())));
        Thread thread1 = new Thread(() -> {
            synchronized (MONITOR) {
                System.out.println(String.format("[%s]-thread1 got monitor lock...", F.format(LocalDateTime.now())));
                try {
                    // Thread.sleep(1000);
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
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println(String.format("[%s]-thread2 releases monitor lock...", F.format(LocalDateTime.now())));
            }
        });
        thread1.start();
        thread2.start();
        // 这里故意让主线程sleep 1500毫秒从而让thread2调用了Object#notify()并且尚未退出同步代码块，确保thread1调用了Object#wait()
        while (true) {
            Thread.sleep(1500);
            System.out.println(thread1.getState());
        }
    }
}