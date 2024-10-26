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

public class NormalPredictionTest {
    public static void main(String[] args) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        // 添加数据
        stats.addValue(10);
        stats.addValue(20);
//        stats.addValue(33);
//        stats.addValue(28);
//        stats.addValue(18);
//        stats.addValue(40);
        System.out.println("均值：" + stats.getMean());
        System.out.println("标准差：" + stats.getStandardDeviation());
        for (int i = 0; i < 10; i++) {
            // 计算Z值
            double z = stats.getMean() + stats.getStandardDeviation();

            System.out.println("Z值(均值+1倍标准差)：" + z);

            NormalDistribution normalDistribution = new NormalDistribution(stats.getMean(), stats.getStandardDeviation());
            System.out.println("正态分布：" + normalDistribution.cumulativeProbability(z));
            stats.addValue(z);
        }
    }
}
