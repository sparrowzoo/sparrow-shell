package com.sparrow.facade.thread.read.write.lock;

public class LeftShiftBitTest {
    public static void main(String[] args) {
        int i =0b10000000000000000000000000000001;
        System.out.println(Integer.toBinaryString(i));
        for (int j = 0; j < Integer.BYTES*8; j++) {
            int k = i << j;
            System.out.println(Integer.toBinaryString(k) + "^" + k + "^" + (k / i));
        }
    }
}
