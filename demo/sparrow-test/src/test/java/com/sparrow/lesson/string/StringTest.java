package com.sparrow.lesson.string;

import java.io.InputStream;

/**
 * com.sparrow.lesson.string.StringTest
 *
 * @author zhanglz
 */

public class StringTest {

    public static void main(String[] args) {
        String str = "hello 张三";

        String str2 = new String("hello 张三2");

        String str3 = str2.intern();

        System.out.println("hello 张三2" == str3);

        System.out.println(str == str2);
        System.out.println(str == str3);

//    Scanner scanner = new Scanner(System.in);
//    System.out.println(scanner.next());
//    System.out.println(scanner.nextDouble());

        InputStream inputStream = StringTest.class.getResourceAsStream("/log.properties");
        System.out.println(inputStream == null);
    }
}
