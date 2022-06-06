package com.sparrow.lesson.lesson2;

/**
 * @author 数据类型的精度
 */
public class PrecisionLesson {

  public static void main(String[] args) {
    int i = Integer.MAX_VALUE;
    System.out.println("int:" + i + ",float=" + (float) i);

    long l = Long.MAX_VALUE;
    System.out.println("long:" + l + ",double:" + (double) l);

    int big=1234567890;
    float approx=(float) big;
    System.out.println(big-(int)approx);
  }
}
