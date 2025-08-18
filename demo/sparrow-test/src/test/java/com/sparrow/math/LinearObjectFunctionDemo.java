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

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.List;

public class LinearObjectFunctionDemo {
    public static void main(String[] args) {

        //创建目标函数
        //2x+3y-5
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{2, 3}, -5);

        //定义约束条件
        //1x+2y<=10
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[]{1, 2}, Relationship.LEQ, 10));
        constraints.add(new LinearConstraint(new double[]{-1, 2}, Relationship.GEQ, 0));
        constraints.add(new LinearConstraint(new double[]{1, 2}, Relationship.EQ, 7));
//constraints.add(new LinearConstraint(, Relationship.EQ, -3));

        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);


        //求解
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(function, constraintSet, GoalType.MAXIMIZE);

        double[] point = solution.getPoint();
        double max = solution.getValue();
        System.out.println("max=" + max);
        System.out.println("point:x=" + point[0]+",y="+point[1]);
    }
}
