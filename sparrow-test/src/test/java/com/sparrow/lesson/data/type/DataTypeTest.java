package com.sparrow.lesson.data.type;

/**
 * @author zhanglz
 */

public class DataTypeTest {

  public static void main(String[] args) {
    System.out.println("byte-min:"+Byte.MIN_VALUE);
    System.out.println("byte-max:"+Byte.MAX_VALUE);

    System.out.println("short-min:"+Short.MIN_VALUE);
    System.out.println("short-max:"+Short.MAX_VALUE);

    System.out.println("int-min:"+Integer.MIN_VALUE);
    System.out.println("int-max:"+Integer.MAX_VALUE);

    System.out.println("long-min:"+Long.MIN_VALUE);
    System.out.println("long-max:"+Long.MAX_VALUE);

    System.out.println("char-min:"+(int)Character.MIN_VALUE);
    System.out.println("char-max:"+(int)Character.MAX_VALUE);

    System.out.println("char-unicode:"+(int)'\u0000');
    System.out.println("char-unicode:"+(int)'\uffff');
  }
}
