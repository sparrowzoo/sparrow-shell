package com.sparrow.facade.thread.read.write.lock.sync;

import org.apache.commons.lang3.StringUtils;

public class ShiftTest {
    public static final int SHARED_SHIFT = 16;
    public static final int SHARED_UNIT = (1 << SHARED_SHIFT);

    public static void main(String[] args) {
        /**
         * 前16位是读锁的个数
         * 后16位是写锁的重入次数
         *
         */
        int state = 0b00000000000000110000000000000000;
        System.out.println(Integer.toBinaryString(SHARED_UNIT));
        state = state + SHARED_UNIT;
        System.out.println("右移前的count" + state);
        int readerCount = state >>> 16;
        System.out.println(readerCount);
        String binaryStr = StringUtils.leftPad(Integer.toBinaryString(readerCount), 32, '0');
        System.out.println(binaryStr);

        System.out.println("MAX-READER-COUNT-" + ((1 << 16) - 1));

    }
}
