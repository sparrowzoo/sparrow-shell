package com.sparrow.lesson.ref.type;

import java.util.Arrays;

/**
 * @author zhanglz
 */

public class RefTypeTest {

  public static void main(String[] args) {
    //matrics 对象
    int[] matrics = new int[]{1, 2};
    /**
     * point 类Point 的对象（实例 instance）
     */
    Point point = new Point(matrics);

    Point nullPoint = null;

    int[] arrays = null;

    String str = "hello world";
    String str2 = str + " zhangsan";
    System.out.println(str2);
  }
}

/**
 * Point 类类型
 */
class Point {

  Point(int[] matrics) {
    this.matrics = matrics;
  }

  //数组类型
  int[] matrics;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Point point = (Point) o;
    return Arrays.equals(matrics, point.matrics);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(matrics);
  }
}

/**
 * 接口类型
 */
interface Move {

  void move(int deltax, int deltay);
}