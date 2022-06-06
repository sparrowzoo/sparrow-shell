package com.sparrow.lesson.lesson1;

/**
 * @author zhanglz
 */

public class BinarySearchSquare {

  public static void main(String[] args) {
    for(int i=20;i<100;i++){
      System.out.println("sqre-"+i+"-"+sqrtUp(i));
    }
  }

  public static int sqrtUp(int x) {
    if (x < 0) {
      throw new IllegalArgumentException("x is less 0");
    }
    if (x == 1) {
      return x;
    }
    int l = 1;
    int h = x / 2;
    int mid = 0;
    while (l <= h) {
      mid = l + (h - l) / 2;
      int square = mid * mid;
      if (square == x) {
        return mid;
      }

      if (square > x) {
        h = mid - 1;
      } else {
        l = mid + 1;
      }
    }
    return mid * mid > x ? mid - 1 : mid;
  }

}
