package com.sparrow.jdk.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleWithFixedDelayTest {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService=Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleWithFixedDelay " +System.currentTimeMillis()/1000);
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1,1,TimeUnit.SECONDS);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleAtFixedRate " +System.currentTimeMillis()/1000);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },1,1,TimeUnit.SECONDS);
    }
}
