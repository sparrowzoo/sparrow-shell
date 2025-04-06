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

package com.sparrow.email;

import com.sparrow.constant.Config;
import com.sparrow.container.ConfigReader;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.utility.StringUtility;

public class DefaultEmailSender extends EmailSender {
    public DefaultEmailSender() {
        ConfigReader configReader = ApplicationContext.getContainer().getBean(ConfigReader.class);
        this.host = configReader.getValue(Config.EMAIL_HOST);
        this.from = configReader.getValue(Config.EMAIL_FROM);
        this.username = configReader.getValue(Config.EMAIL_USERNAME);

        String emailPassword = System.getenv(Config.EMAIL_PASSWORD);
        if (StringUtility.isNullOrEmpty(emailPassword)) {
            emailPassword = configReader.getValue(Config.EMAIL_PASSWORD);
        }
        this.password = emailPassword;
        this.localAddress = configReader.getValue(Config.EMAIL_LOCAL_ADDRESS);
    }
}
