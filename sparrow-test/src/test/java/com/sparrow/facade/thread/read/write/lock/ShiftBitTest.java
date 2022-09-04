package com.sparrow.facade.thread.read.write.lock;

import org.apache.commons.lang3.StringUtils;

public class ShiftBitTest {
    public static void main(String[] args) {
        //算术s右移的规则只记住一点：符号位不变，左边补上符号位
//        int i = Integer.MIN_VALUE;
        int i=-678;
        System.out.println(Integer.toBinaryString(i));
        for (int j = 0; j < 32; j++) {
            int k = i >> j;
            //int k = i >> j;
            String binaryStr = StringUtils.leftPad(Integer.toBinaryString(k), 32, '0');
            System.out.println(binaryStr + "|"+k);
        }
    }
}
