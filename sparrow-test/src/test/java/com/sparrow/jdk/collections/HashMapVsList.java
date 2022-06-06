package com.sparrow.jdk.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapVsList {
    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        Map<String,Integer> map=new HashMap<>();
        for(int i=0;i<100000;i++){
            list.add(i+"");
        }

        for(int i=0;i<100000;i++){
            map.put(i+"",i);
        }
        for(int j=0;j<10;j++) {
            long t = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                list.contains(i);
            }
            System.out.println(System.currentTimeMillis() - t);

            t = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                map.containsKey(i + "");
            }
            System.out.println(System.currentTimeMillis() - t);

        }
    }
}
