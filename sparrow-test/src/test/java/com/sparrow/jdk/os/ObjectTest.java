package com.sparrow.jdk.os;

import com.sparrow.jdk.volatilekey.User;

/**
 * @author by harry
 */
public class ObjectTest {
    public static void main(String[] args) {
        User o=new User(1);
        User o1=o;
        System.out.println(o.hashCode());
        o.setAge(222);
        System.out.println(o.hashCode());
    }
}
