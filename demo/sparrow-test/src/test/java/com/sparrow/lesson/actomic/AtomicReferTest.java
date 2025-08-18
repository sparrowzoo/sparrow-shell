package com.sparrow.lesson.actomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferTest {

  public static void main(String[] args) {
    AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater
        .newUpdater(Dog.class, String.class, "name");
    Dog dog1 = new Dog();
    System.out.println(updater.compareAndSet(dog1, "dog1", "compareAndSet"));
    System.out.println(dog1.name);
    System.out.println(updater.getAndSet(dog1, "getAndSet"));
    System.out.println(dog1.name);
  }
}

class Dog {

  volatile String name = "dog1";
}  