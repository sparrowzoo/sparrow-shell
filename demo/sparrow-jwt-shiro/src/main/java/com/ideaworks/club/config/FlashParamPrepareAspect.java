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

package com.ideaworks.club.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FlashParamPrepareAspect implements Ordered {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
             "@annotation(org.springframework.web.bind.annotation.RequestMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
//    @Pointcut("execution(* com.ideaworks.club.controller.*.*(..))")

    public void flashParamPointCut() {
    }

    @Around("flashParamPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = point.getTarget().getClass().getName() + "." + signature.getName();
        System.out.println("FlashParamPrepareAspect.around() called for method: " + methodName);
        Object result = point.proceed();
        System.out.println("FlashParamPrepareAspect.around() result: " + result);
        return result;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
