package com.sparrow.jdk.chars;

/**
 * @author: zhanglizhi@kanzhun.com
 * @date: 2019-03-27 16:40
 * @description:
 */
public class ReplacePerformentTest {
    public static void main(String[] args) {
        String str = "hello world";
        Long current = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            if (str.contains("not exit")) {
                str.replace("not exist", "");
            }
        }
        System.out.println(System.currentTimeMillis() - current);


        current = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            str.replace("not exist", "");
        }
        System.out.println(System.currentTimeMillis() - current);
    }
}
