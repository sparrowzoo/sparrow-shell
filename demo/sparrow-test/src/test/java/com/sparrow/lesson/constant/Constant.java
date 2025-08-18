package com.sparrow.lesson.constant;

/**
 * @author zhanglz
 */

public class Constant {

  public static void main(String[] args) {
    //十进制
    int i = 10;
    //十六进制 0 1 2 3 4 5 6 .... F
    int hex = 0xffff;
    //八进制 0 1 2 3 3 5 6 7
    int oct = 07777;
    //二进制 0 1
    int binary = 0b10101010101;

    System.out.println(Integer.toBinaryString(binary));
    System.out.println(Integer.toBinaryString(hex));
    System.out.println(Integer.toHexString(binary));
    //true false  为boolean常量
    boolean b = true;
    //unicode 字符常量
    char c='\u2122';
    String str = "张三学  Java\u2122";
    System.out.println(str);

    //单精度浮点数
    float f = 1.0F;
    //双精度浮点数
    double d = 1.0D;
    //如果一个浮点字面量有后缀F,f 就是float,否则就是double
    System.out.println(1.0);
  }

  private void print(String... args) {
    System.out.println(args);
  }
}
