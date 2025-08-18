package com.sparrow.lesson.object.type;

/**
 * @author zhanglz
 */

public class MultiThreadObjectTest {

  /**
   * monitor
   */
  private static Object lock = new Object();

  public static void main(String[] args) {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (lock) {
          try {
            System.out.println("wait");
            lock.wait();
            System.out.println("wait finish");
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    thread.start();
    thread.interrupt();
    System.out.println(thread.isInterrupted());


    Thread threadNotify = new Thread(new Runnable() {
      @Override
      public void run() {
        synchronized (lock) {
          System.out.println("notify");
          lock.notifyAll();
        }
      }
    });
    threadNotify.start();
  }
}
