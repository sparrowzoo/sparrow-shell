package com.sparrow.lesson.lesson1;

import java.util.concurrent.atomic.AtomicStampedReference;

public class SolveCAS {


  public static void main(String[] args) {
    AtomicStampedReference<Integer> atomicStampedReference = new
        AtomicStampedReference<>(1, 1);
    // 第一次拿到的时间戳
    int stamp = atomicStampedReference.getStamp();
    boolean b = atomicStampedReference.compareAndSet(1, 10, stamp, stamp + 1);

    // 第二次拿到的时间戳(这里是多线程执行)
    int stamp2 = atomicStampedReference.getStamp();
    // 将副本改为20，再写入，紧接着又改为1，写入，每次提升一个时间戳，中间t1没介入
    atomicStampedReference.compareAndSet(1, 20, stamp2, stamp2 + 1);
  }
}