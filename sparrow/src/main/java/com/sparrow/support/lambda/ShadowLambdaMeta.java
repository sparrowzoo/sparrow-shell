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

package com.sparrow.support.lambda;


import com.sparrow.cg.PropertyNamer;
import com.sparrow.protocol.constant.magic.Symbol;

import java.lang.invoke.SerializedLambda;

public class ShadowLambdaMeta implements LambdaMeta {
    private final SerializedLambda lambda;
    private String className;
    private String methodName;

    public ShadowLambdaMeta(SerializedLambda lambda) {
        this.lambda = lambda;
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        this.className = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(Symbol.SEMICOLON)).replace(Symbol.SLASH, Symbol.DOT);
        this.methodName = lambda.getImplMethodName();
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    @Override
    public String getPropertyName() {
        return PropertyNamer.methodToProperty(methodName);
    }
}
