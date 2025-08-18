package com.sparrow.lesson.data.type;

/**
 * @author zhanglz
 */

public class IntTest {

    public static void main(String[] args) {
        byte b1 = 127;
        short b2 = 127;
        //非long 类型的整数去处会扩宽至int 型
        System.out.println((b1 + b2));

        int i = 0;
        //整数类型与布尔类型无法强制转换
        //boolean b=(boolean) i;
        boolean b = false;
        //int ii=(int)b;
    }
}
