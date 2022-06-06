package com.sparrow.lesson.object.type;

/**
 * @author zhanglz
 */

public class ObjectTest {

  public static void main(String[] args) throws CloneNotSupportedException {
    String str = "hello world";
    Object o = str;
    System.out.println(o.equals("hello world"));

    System.out.println(str.getClass());
    System.out.println(o.getClass());

    System.out.println(str.hashCode());
    System.out.println(str.toString());
  }
}
