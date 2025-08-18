package com.sparrow.jdk.os;

import java.util.HashSet;
import java.util.Set;

/**
 * @author by harry
 */
public class RandomTest {
    public static void main(String[] args) {
        //return distinct integers for distinct objects
        Set<Integer> s = new HashSet<>();
        int i = 0;
        while (true) {
            Object o = new Object();
            if (!s.contains(o.hashCode())) {
                s.add(o.hashCode());
            } else {
                System.out.println(o.hashCode());
            }
        }
    }
}
