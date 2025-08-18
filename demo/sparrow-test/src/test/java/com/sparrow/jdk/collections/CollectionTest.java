package com.sparrow.jdk.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionTest {
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        Map<Integer,Object> map=new ConcurrentHashMap<>();
        for(int i=0;i<10000000;i++){
            map.put(i,i);
        }
        long t=System.currentTimeMillis();
        for(Integer key:map.keySet()){
            if(key>1000&&key<100000){
                map.remove(key);
            }
        }
        System.out.println((System.currentTimeMillis()-t)+"");
        t=System.currentTimeMillis();
        List<Integer> deletingKeys=new ArrayList<>();
        for(Integer key:map.keySet()){
            if(key>1000&&key<100000){
               deletingKeys.add(key);
            }
        }
        for(Integer key:deletingKeys){
            map.remove(key);
        }
        System.out.println((System.currentTimeMillis()-t)+"");
    }
}
