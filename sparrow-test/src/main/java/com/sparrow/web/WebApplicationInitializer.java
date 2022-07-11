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
package com.sparrow.web;

import com.sparrow.container.Container;
import com.sparrow.support.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationInitializer implements Initializer {

    private final Logger logger = LoggerFactory.getLogger(WebApplicationInitializer.class);

    @Override
    public void init(Container container) {
        logger.info("welcome to web application initializer 你可以在这里初始化任何东西，这里的容器已经启动了，神马都准备好了... 我在这 container-->" + container);
    }

    @Override
    public void destroy(Container container) {

    }
}
