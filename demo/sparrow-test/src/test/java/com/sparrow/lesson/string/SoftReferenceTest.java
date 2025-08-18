package com.sparrow.lesson.string;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class SoftReferenceTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String a = "Hello, reference";
    List<Byte[]> cache=new ArrayList<>();
    SoftReference<String> sr = new SoftReference<String>(a);
    int i = 0;
    while (sr.get() != null) {
      Byte bytes[]=new Byte[1024*1024*100];

      cache.add(bytes);
      System.out.println(
          String.format("Get str from object of SoftReference: %s, count: %d", sr.get(), ++i));
      if (i % 5 == 0) {
        System.gc();
        System.out.println("System.gc() was invoked!");
      }
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {

      }
    }
    System.out.println("object a was cleared by JVM!");
  }

}  