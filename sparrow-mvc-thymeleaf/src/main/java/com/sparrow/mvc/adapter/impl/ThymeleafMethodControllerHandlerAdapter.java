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

package com.sparrow.mvc.adapter.impl;

import com.sparrow.mvc.result.MethodReturnValueResolverHandler;
import com.sparrow.mvc.result.ThymeleafViewWithModelMethodReturnValueResolverHandler;
import com.sparrow.mvc.result.impl.JsonMethodReturnValueResolverHandlerImpl;
import com.sparrow.mvc.result.impl.MethodReturnValueResolverHandlerComposite;
import com.sparrow.mvc.result.impl.ViewWithModelMethodReturnValueResolverHandlerImpl;
import com.sparrow.mvc.result.impl.VoidReturnValueResolverHandlerImpl;
import java.util.ArrayList;
import java.util.List;

public class ThymeleafMethodControllerHandlerAdapter extends MethodControllerHandlerAdapter{
    @Override protected void initReturnValueResolvers() {
        this.returnValueResolverHandlerComposite = new MethodReturnValueResolverHandlerComposite();
        List<MethodReturnValueResolverHandler> methodReturnValueResolverHandlers = new ArrayList();
        MethodReturnValueResolverHandler viewWithModelMethodReturnValueResolverHandler = new ThymeleafViewWithModelMethodReturnValueResolverHandler();

        MethodReturnValueResolverHandler jsonMethodReturnValueResolverHandler = new JsonMethodReturnValueResolverHandlerImpl();
        MethodReturnValueResolverHandler voidMethodReturnValueResolverHandler = new VoidReturnValueResolverHandlerImpl();
        methodReturnValueResolverHandlers.add(jsonMethodReturnValueResolverHandler);
        methodReturnValueResolverHandlers.add(viewWithModelMethodReturnValueResolverHandler);
        methodReturnValueResolverHandlers.add(voidMethodReturnValueResolverHandler);
        this.returnValueResolverHandlerComposite.addResolvers(methodReturnValueResolverHandlers);
    }
}
