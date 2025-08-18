package com.sparrow.jdk.chars;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhanglizhi@kanzhun.com
 * @date: 2019-03-27 16:40
 * @description:
 */
public class ReplacePerformentForHashTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        for(int i=0;i<1000;i++) {
            map.put("hello"+i, "world");
        }
        Long current = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            if (map.containsKey("not exit")) {
                map.replace("not exist", "");
            }
        }
        System.out.println(System.currentTimeMillis() - current);


        current = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            map.replace("not exist", "");
        }
        System.out.println(System.currentTimeMillis() - current);
    }
}
