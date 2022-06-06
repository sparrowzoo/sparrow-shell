package com.sparrow.utility;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zh_harry@163.com
 * @date: 2019-04-07 17:45
 * @description:
 */
public class BigIntegerUtility {

    private static final int DEFAULT_LENGTH = 64;
    private static final int SEGMENT_SIZE = 4;

    /**
     * split 4 segments for simhash
     *
     * @param simHash sim hash
     */
    public static List<BigInteger> split4Segment(BigInteger simHash) {
        return split4Segment(simHash, 0);
    }

    public static List<BigInteger> split4Segment(BigInteger simHash, int bitLength) {
        if (bitLength <= 0) {
            bitLength = DEFAULT_LENGTH;
        }
        int segmentLength = bitLength / SEGMENT_SIZE;
        List<BigInteger> segments = new ArrayList<BigInteger>(SEGMENT_SIZE);
        StringBuilder buffer = new StringBuilder(segmentLength);
        for (int i = 0; i < bitLength; i++) {
            buffer.append(simHash.testBit(i) ? "1" : "0");
            if ((i + 1) % segmentLength == 0) {
                segments.add(new BigInteger(buffer.toString(), 2));
                buffer.setLength(0);
            }
        }
        return segments;
    }
}
