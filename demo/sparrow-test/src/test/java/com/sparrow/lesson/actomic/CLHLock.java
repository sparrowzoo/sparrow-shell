package com.sparrow.lesson.actomic;// CLHLock.java

import java.util.concurrent.atomic.AtomicReference;

/**
 * https://mp.weixin.qq.com/s/xBw7koGuZtqU8imZ9_JzDA
 */
public class CLHLock {

  public static void main(String[] args) {
    CLHLock lock = new CLHLock();

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();

      }
    });
    thread.setName("T1");
    thread.start();

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.lock();
        lock.unLock();
        lock.lock();

      }
    });
    thread2.setName("T2");
    thread2.start();
//
//    Thread thread3 = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        lock.lock();
//
//      }
//    });
//    thread3.setName("T3");
//    thread3.start();
  }

  /**
   * CLH锁节点
   */
  private static class CLHNode {

    // 锁状态：默认为false，表示线程没有获取到锁；true表示线程获取到锁或正在等待
    // 为了保证locked状态是线程间可见的，因此用volatile关键字修饰
    volatile boolean locked = false;
    String threadName = "";

    public CLHNode() {
      this.threadName = Thread.currentThread().getName();
    }
  }

  // 尾结点，总是指向最后一个CLHNode节点
  // 【注意】这里用了java的原子系列之AtomicReference，能保证原子更新
  private final AtomicReference<CLHNode> tailNode;
  // 当前节点的前继节点
  private final ThreadLocal<CLHNode> predNode;
  // 当前节点
  private final ThreadLocal<CLHNode> curNode;

  // CLHLock构造函数，用于新建CLH锁节点时做一些初始化逻辑
  public CLHLock() {
    // 初始化时尾结点指向一个空的CLH节点
    tailNode = new AtomicReference<>(new CLHNode());
    // 初始化当前的CLH节点
    curNode = new ThreadLocal() {
      @Override
      protected CLHNode initialValue() {
        System.out.println("init NODE" + Thread.currentThread().getName());
        return new CLHNode();
      }
    };
    // 初始化前继节点，注意此时前继节点没有存储CLHNode对象，存储的是null
    predNode = new ThreadLocal();
  }

  /**
   * 获取锁
   */
  public void lock() {
    // 取出当前线程ThreadLocal存储的当前节点，初始化值总是一个新建的CLHNode，locked状态为false。
    CLHNode currNode = curNode.get();
    // 此时把lock状态置为true，表示一个有效状态，
    // 即获取到了锁或正在等待锁的状态
    currNode.locked = true;
    // 当一个线程到来时，总是将尾结点取出来赋值给当前线程的前继节点；
    // 然后再把当前线程的当前节点赋值给尾节点
    // 【注意】在多线程并发情况下，这里通过AtomicReference类能防止并发问题
    // 【注意】哪个线程先执行到这里就会先执行predNode.set(preNode);语句，因此构建了一条逻辑线程等待链
    // 这条链避免了线程饥饿现象发生
    CLHNode tailNode = this.tailNode.getAndSet(currNode);
    // 将刚获取的尾结点（前一线程的当前节点）付给当前线程的前继节点ThreadLocal
    // 【思考】这句代码也可以去掉吗，如果去掉有影响吗？
    System.out.println(
        "CurrentThread=" + Thread.currentThread().getName() + "PreThread:" + tailNode.threadName);
    // 设置当前线程的上一个节点
    predNode.set(tailNode);
    // 【1】若前继节点的locked状态为false，则表示获取到了锁，不用自旋等待；
    // 【2】若前继节点的locked状态为true，则表示前一线程获取到了锁或者正在等待，自旋等待
    while (tailNode.locked) {
      System.out.println("线程" + Thread.currentThread().getName() + "没能获取到锁，进行自旋等待。。。");
    }

    // 能执行到这里，说明当前线程获取到了锁
    System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁！！！");
  }

  /**
   * 释放锁
   */
  public void unLock() {
    // 获取当前线程的当前节点
    CLHNode node = curNode.get();
    // 进行解锁操作
    // 这里将locked至为false，此时执行了lock方法正在自旋等待的后继节点将会获取到锁
    // 【注意】而不是所有正在自旋等待的线程去并发竞争锁
    node.locked = false;
    System.out.println("线程" + Thread.currentThread().getName() + "释放了锁！！！");
  }
}