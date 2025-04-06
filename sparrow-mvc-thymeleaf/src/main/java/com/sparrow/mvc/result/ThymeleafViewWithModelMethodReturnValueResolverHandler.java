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

package com.sparrow.mvc.result;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.mvc.PageSwitchMode;
import com.sparrow.mvc.ThymeleafEngineUtils;
import com.sparrow.mvc.result.impl.ViewWithModelMethodReturnValueResolverHandlerImpl;
import com.sparrow.protocol.constant.Extension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThymeleafViewWithModelMethodReturnValueResolverHandler extends ViewWithModelMethodReturnValueResolverHandlerImpl {
    @Override
    protected void forward(HttpServletRequest request, HttpServletResponse response,
                           String url) throws IOException {
        ThymeleafEngineUtils.forward(request, response, url);
    }

    @Override
    protected String assembleUrl(String referer, String defaultSuccessUrl, String url, PageSwitchMode pageSwitchMode,
                                 String[] urlArgs) {
        String assembleUrl = super.assembleUrl(referer, defaultSuccessUrl, url, pageSwitchMode, urlArgs);
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        String pagePrefix = configReader.getValue(Config.TEMPLATE_ENGINE_PREFIX, "/template");
        String extension = configReader.getValue(Config.TEMPLATE_ENGINE_SUFFIX, Extension.HTML);

        if (assembleUrl.startsWith(pagePrefix)) {
            assembleUrl = assembleUrl.substring(pagePrefix.length());
        }
        if (assembleUrl.endsWith(extension)) {
            assembleUrl = assembleUrl.substring(0, assembleUrl.indexOf(extension));
        }
        return assembleUrl;
    }
}
