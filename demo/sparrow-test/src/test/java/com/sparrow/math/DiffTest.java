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

public class DiffTest {
    public static void main(String[] args) {
        double forecastF = 3.75;
        double actualF = 4.0;
        //double[] x = {15, 14, 12, 11};
        //double[] x = {10, 11, 12, 13};
        double[] x = {10, 10, 10, 10};


        double lastX = x[x.length - 1] * 0.75;
        x[x.length - 1] = lastX;
        double forecastQ = 0D;
        for (int i = 0; i < x.length; i++) {
            forecastQ += x[i];
        }
        System.out.println("forecastQ=" + forecastQ);


        double ratio = forecastF / actualF;

        forecastQ = 0D;
        for (int i = 0; i < x.length; i++) {
            forecastQ += x[i] * ratio;
        }
        System.out.println("ratio forecastQ=" + forecastQ);


        forecastQ = 0D;
        for (int i = 0; i < x.length; i++) {
            forecastQ += x[i];
        }
        forecastQ *= ratio;
        System.out.println("ratio forecastQ=" + forecastQ);
    }
}
