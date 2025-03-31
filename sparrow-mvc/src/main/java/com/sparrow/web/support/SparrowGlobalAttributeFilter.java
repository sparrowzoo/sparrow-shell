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
import com.sparrow.support.AttributeContext;
import com.sparrow.support.web.AbstractGlobalAttributeFilter;
import com.sparrow.utility.ConfigUtility;

public class SparrowGlobalAttributeFilter extends AbstractGlobalAttributeFilter {
    @Override
    public AttributeContext parseAttributeContext() {
        return new AttributeContext() {
            @Override
            public String getRootPath() {
                return ConfigUtility.getValue(Config.ROOT_PATH);
            }

            @Override
            public String getLanguage() {
                return ConfigUtility.getValue(Config.INTERNATIONALIZATION);
            }


            @Override
            public String getResource() {
                return ConfigUtility.getValue(Config.RESOURCE);
            }

            @Override
            public String getResourceVersion() {
                return ConfigUtility.getValue(Config.RESOURCE_VERSION);
            }

            @Override
            public String getUpload() {
                return ConfigUtility.getValue(Config.UPLOAD);
            }

            @Override
            public String getInternationalization() {
                return ConfigUtility.getValue(Config.INTERNATIONALIZATION);
            }
        };
    }
}
