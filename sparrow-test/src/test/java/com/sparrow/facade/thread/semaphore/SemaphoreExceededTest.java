package com.sparrow.facade.thread.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExceededTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        semaphore.release(Integer.MAX_VALUE);
    }
}
