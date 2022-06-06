package com.sparrow.lesson.constant;

/**
 * @author zhanglz
 */

public class StrictFpTest {


  public strictfp static void main(String[] args) {
    float f1 = 0.1F;
    float f2 = 0.2F;
    System.out.println((float)(f1 + f2));
  }
}
