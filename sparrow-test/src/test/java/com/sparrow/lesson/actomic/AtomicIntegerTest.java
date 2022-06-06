package com.sparrow.lesson.actomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicReferenceFieldUpdater是Doug Lea在Java 5中
 *
 * 写的atomic classes 中Filed Updater的一部分， 本质上是volatile字段的包装器。
 *
 * 相似的还有AtomicIntegerFieldUpdater
 *
 * AtomicReferenceFieldUpdater是基于反射的工具类，
 *
 * 用来将指定类型的指定的volatile引用字段进行原子更新，
 *
 * 原子引用字段必须是区域可见的（比如不能是private的）
 *
 * 操作的字段不能是static类型。
 *
 * 操作的字段不能是final类型的，因为final根本没法修改。
 *
 * 不能是非int 类型（Integer类型也允许）
 */
public class AtomicIntegerTest {

  public static void main(String[] args) {
    AtomicIntegerFieldUpdater<Cat> updater = AtomicIntegerFieldUpdater
        .newUpdater(Cat.class, "age");
    Cat cat = new Cat();
    System.out.println(updater.compareAndSet(cat, 100, 101));
    System.out.println(cat.age);
    System.out.println(updater.getAndSet(cat, 102));
    System.out.println(cat.age);
  }

  static class Cat {

    private volatile int age = 100;
  }
}

