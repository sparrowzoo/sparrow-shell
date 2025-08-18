package com.sparrow.lesson.thread.blocking;

public class MainThreadWhile {
    public static void main(String[] args) throws InterruptedException {
        Thread interrupterThread = new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
//                    if (Thread.currentThread().isInterrupted()) {
//                        System.out.println("do something for business");
//                        break;
//                    }
                    //CAS COMPARE AND SET
                    System.out.println(Thread.currentThread().getName() + "-state" + Thread.currentThread().getState() + " isInterrupted:" + Thread.currentThread().isInterrupted());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "interrupter-thread");
        interrupterThread.start();
//        interrupterThread.stop();
//        interrupterThread.destroy();
        interrupterThread.interrupt();
        System.out.println(interrupterThread.getState());
    }
}
