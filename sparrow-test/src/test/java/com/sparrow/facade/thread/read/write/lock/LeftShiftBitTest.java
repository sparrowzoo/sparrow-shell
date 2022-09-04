package com.sparrow.facade.thread.read.write.lock;

import com.sparrow.utility.StringUtility;

public class LeftShiftBitTest {
    public static void main(String[] args) {
        int i = -678;
        System.out.println(Integer.toBinaryString(i));
        for (int j = 0; j < Integer.BYTES * 8; j++) {
            int k = i << j;
            System.out.println(Integer.toBinaryString(k) + "^" + k + "^" + (k / i));
        }

        i=678;
        System.out.println(Integer.toBinaryString(i));
        for (int j = 0; j < Integer.BYTES * 8; j++) {
            int k = i << j;
            System.out.println(StringUtility.leftPad(Integer.toBinaryString(k),'0',32) + "^" + k + "^" + (k / i));
        }
    }
}
