package com.sparrow.facade.thread.read.write.lock;

public class ShiftBitTest {
    public static void main(String[] args) {
        //右移的规则只记住一点：符号位不变，左边补上符号位
        int i = Integer.MIN_VALUE;
        System.out.println(Integer.toBinaryString(i));
        System.out.println(i >> 2);
        System.out.println(i >>> 2);
        for (int j = 0; j < 32; j++) {
            int k = i >>> j;
            System.out.println(Integer.toBinaryString(k) + "-" + (i / k));
        }
    }
}
