package com.sparrow.lesson.data.type;

/**
 * @author zhanglz
 */
public class BooleanOperatorTest {

    public static void main(String[] args) {
        boolean b = true;
        System.out.println(b & false);
        System.out.println(b && false);

        System.out.println(b | false);
        System.out.println(b || false);
    }
}
