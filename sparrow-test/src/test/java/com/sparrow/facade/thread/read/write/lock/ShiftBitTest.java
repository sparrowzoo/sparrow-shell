package com.sparrow.facade.thread.read.write.lock;

import org.apache.commons.lang3.StringUtils;

public class ShiftBitTest {
    public static void main(String[] args) {
        //右移的规则只记住一点：符号位不变，左边补上符号位
        int i = Integer.MIN_VALUE;
        System.out.println(Integer.toBinaryString(i));
        //System.out.println(i >> 2);
        //System.out.println(i >>> 2);
        for (int j = 0; j < 32; j++) {
            int k = i >>> j;
            String num=Integer.toBinaryString(k);
            num= StringUtils.leftPad(num,32,'0');
            System.out.println(num + "-" + (i / k));
        }
    }
}
