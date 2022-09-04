package com.sparrow.facade.thread.read.write.lock;

import org.apache.commons.lang3.StringUtils;

public class LeftShiftBit1Test {
    public static void main(String[] args) {
        int i = 0b10000000000011111000000000000001;
        i = i << 1;
        String binary32 = StringUtils.leftPad(Integer.toBinaryString(i), 32, '0');
        System.out.println(binary32);
    }
}