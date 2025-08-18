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
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatisticsExample {
    public static void main(String[] args) {
        DescriptiveStatistics stats = new DescriptiveStatistics();

        // 添加数据
        stats.addValue(1);
        stats.addValue(2);
        stats.addValue(3);
        stats.addValue(4);
        stats.addValue(100);
        stats.addValue(86);

        stats.addValue(9);
        stats.addValue(8);
        stats.addValue(5);
        stats.addValue(2);
        stats.addValue(2);
        stats.addValue(2);
        stats.addValue(2);
        stats.addValue(2);
        stats.addValue(2);


        // 计算均值
        double mean = stats.getGeometricMean();
        System.out.println("Geometric Mean: " + mean);

        // 计算标准差
        double stdDev = stats.getStandardDeviation();
        System.out.println("Standard Deviation: " + stdDev);

        NormalDistribution normalDistribution = new NormalDistribution(mean, stdDev);
        double p = normalDistribution.cumulativeProbability(mean + 3 * stdDev);
        System.out.println("Probability of 3 standard deviations: " + p);
        System.out.println(normalDistribution.inverseCumulativeProbability(p) + "vs" + (mean + 3 * stdDev));
    }
}
