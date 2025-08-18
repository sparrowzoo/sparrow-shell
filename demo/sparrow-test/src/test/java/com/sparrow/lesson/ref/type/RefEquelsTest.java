package com.sparrow.lesson.ref.type;

/**
 * @author zhanglz
 */

public class RefEquelsTest {

  public static void main(String[] args) {
    Integer i = 100;
    Integer j = 100;
    System.out.println(i == j);
    System.out.println(i.equals(j));

    i = 1000;
    j = 1000;
    System.out.println(i == j);
    System.out.println(i.equals(j));
  }
}
