/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.utility;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
