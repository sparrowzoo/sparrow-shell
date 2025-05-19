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

package com.sparrow.support.web;

import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ResultI18nMessageAssembler;

public class DefaultResultI18nMessageAssembler implements ResultI18nMessageAssembler {
    @Override
    public String assemble(BusinessException exception) {
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        String error = configReader.getI18nValue(exception.getKey(), null, exception.getMessage());
        if (exception.getKey().contains(".")) {
            String ctrlName = exception.getKey().split("\\.")[1];
            HttpContext.getContext().put(ctrlName, error);
        }
        return error;
    }

    @Override
    public String assemble(String originMessage, String i18nKey) {
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        return configReader.getI18nValue(i18nKey, null, originMessage);
    }
}
