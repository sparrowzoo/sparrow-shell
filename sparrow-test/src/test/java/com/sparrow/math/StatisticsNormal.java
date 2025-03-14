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

import org.apache.commons.math3.distribution.NormalDistribution;

public class StatisticsNormal {
    public static void main(String[] args) {
        NormalDistribution normalDistribution = new NormalDistribution(10, 1);
        for (int i = 1; i <= 10; i++) {
            double p=i/10D;
            double z = normalDistribution.inverseCumulativeProbability(p);
            System.out.println("p=" + p + "\tz=" + z);
        }
        //System.out.println(normalDistribution.cumulativeProbability(z));
    }
}
