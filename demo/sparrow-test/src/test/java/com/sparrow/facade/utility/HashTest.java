package com.sparrow.facade.utility;

import java.util.HashMap;
import java.util.Map;

public class HashTest {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(100, 1);
        map.put(200, 2);
        map.put(300, 3);
        map.put(400, 4);
        map.put(500, 5);
        for (int i = 0; i < 10; i++) {
            System.out.println("round" + i);
            System.out.println(map.keySet());
            System.out.println(map.values());
        }
    }
}
