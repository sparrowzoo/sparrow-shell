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

package com.sparrow.web.support;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.support.AttributeContext;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;

public class SparrowGlobalAttributeFilter extends AbstractGlobalAttributeFilter {
    @Override
    public AttributeContext parseAttributeContext() {

        return new AttributeContext() {
            private ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);

            @Override
            public String getRootPath() {
                return configReader.getValue(Config.ROOT_PATH);
            }

            @Override
            public String getLanguage() {
                return configReader.getValue(Config.INTERNATIONALIZATION);
            }


            @Override
            public String getResource() {
                return configReader.getValue(Config.RESOURCE);
            }

            @Override
            public String getResourceVersion() {
                return configReader.getValue(Config.RESOURCE_VERSION);
            }

            @Override
            public String getUpload() {
                return configReader.getValue(Config.UPLOAD);
            }

            @Override
            public String getInternationalization() {
                return configReader.getValue(Config.INTERNATIONALIZATION);
            }
        };
    }
}
