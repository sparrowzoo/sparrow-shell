/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.labmda;


import java.lang.invoke.SerializedLambda;

/**
 * 基于创建的元信息
 * <p>
 * Create by hcl at 2021/7/7
 */
public class ShadowLambdaMeta implements LambdaMeta {
    private final SerializedLambda lambda;

    public ShadowLambdaMeta(SerializedLambda lambda) {
        this.lambda = lambda;
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

    @Override
    public String getInstantiatedClass() {
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        return instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(StringPool.SEMICOLON)).replace(StringPool.SLASH, StringPool.DOT);
    }

}
