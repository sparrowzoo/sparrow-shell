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
package com.sparrow.container.config;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.datasource.DatasourceConfigReader;

public class SparrowDatasourceConfigReader implements DatasourceConfigReader {
    ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);

    @Override
    public String getDefaultSchema() {
        return configReader.getValue(Config.DEFAULT_DATA_SOURCE_KEY);
    }

    @Override
    public String getPasswordKey() {
        return configReader.getValue(Config.DATA_SOURCE_PASSWORD_KEY);
    }

    @Override
    public Boolean getDebugDatasourcePassword() {
        return configReader.getBooleanValue(Config.DEBUG_DATA_SOURCE_PASSWORD_KEY,false);
    }
}
