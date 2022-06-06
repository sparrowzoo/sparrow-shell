package com.sparrow.lesson.lesson1;

/**
 * 输出0-10
 *
 * @author harry
 */

public class HelloWorld3 {

  public static void main(String[] args) {
    //sortStruction();
    //whileStruction();
    for (int i = 0; i <= 10; i++) {
      if (i % 2 == 1) {
        System.out.println(i);
      }
    }
  }

  /**
   * y=3*x
   */
  private static void whileStruction() {
    for (int i = 0; i <= 10; i++) {
      System.out.println(i);
    }
  }

  private static void sortStruction() {
    int i = 0;
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
    System.out.println(i++);
  }
}
