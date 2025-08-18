package com.sparrow.lesson.lesson3;

/**
 * @author zhanglz
 */

public class ConvertException {

  static class Point {

    int x;
    int y;
  }

  /**
   * FP 严格的
   */
  static strictfp class ColorRedPoint extends Point {

    int color;
  }

  public static void main(String[] args) {
    Object[] objects = new Point[1000];
    Point[] points = new Point[100];
    //points = new ColorRedPoint[100];

    points[0] = new Point();

    System.out.println("#零基础学编程 #java程序员 刚入行的你对并发编程头大吗？这本书可以说java程序员人手一本，良心推荐。".length());
  }

}
