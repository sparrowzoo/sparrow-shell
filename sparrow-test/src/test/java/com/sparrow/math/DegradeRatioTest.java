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

package com.sparrow.math;

import java.util.Map;

public class DegradeRatioTest {
    public static void main(String[] args) {
        int t0 = 0;
        int tn = 8;
        int initValue = 1;
        double endValue = 0;
        double degradeRatio =0.3;

        for (int i = t0; i <= tn; i++) {
            double value = initValue * Math.pow(Math.E, -degradeRatio * (i - t0));
            System.out.println("t=" + i + ", value=" + value);
        }
    }
}
