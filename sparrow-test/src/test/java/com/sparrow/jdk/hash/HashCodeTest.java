package com.sparrow.jdk.hash;

import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;

public class HashCodeTest {

    public static void main(String[] args) {
        Obj o1 = new Obj("zhangsan");
        Obj o2 = new Obj("zhangsan");

        Map<String, Obj> map = new HashMap<>(3);
        map.put("a", o1);
        map.put("b", o2);
        map.put("c", o1);

        Map<Obj, String> map1 = new HashMap<>();
        map1.put(o1, "a");
        map1.put(o2, "b");
        System.out.println(o1.equals(o2));
    }
}
