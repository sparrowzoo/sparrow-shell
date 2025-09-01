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
package com.sparrow.authenticator.config;

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.RegexUtility;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ToString
@ConfigurationProperties(prefix = "sparrow.authc")
public class AuthenticatorConfig implements AuthenticatorConfigReader {
    private String tokenKey = Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN;
    private String encryptKey;
    private Boolean validateHost = true;
    private Boolean validateStatus = true;
    private List<String> excludePatterns;
    /**
     * 平台管理员类型(由上层业务自定义)
     */
    private Integer platformManagerCategory;
    private Boolean mockLoginUser = false;
    private Double tokenAvailableDays = 7D;
    private Double rememberMeDays;
    private Long sessionTimeout;
    private Long renewalInterval;
    private String jwtSecret;
    private String jwtIssuer;
    private Boolean renewal;

    public void setExcludePatterns(List<String> excludePatterns) {
        this.excludePatterns = RegexUtility.adapterWildcard(excludePatterns);
    }
}
