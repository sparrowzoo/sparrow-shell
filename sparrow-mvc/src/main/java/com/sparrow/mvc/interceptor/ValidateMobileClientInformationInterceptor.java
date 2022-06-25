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
package com.sparrow.mvc.interceptor;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.StringUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateMobileClientInformationInterceptor extends SimpleMobileClientInformationInterceptor {
    private Logger logger = LoggerFactory.getLogger(SimpleMobileClientInformationInterceptor.class);

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.preHandle(request, response);
        ClientInformation clientInformation = (ClientInformation) request.getAttribute(Constant.REQUEST_CLIENT_INFORMATION);

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getAppId()), "app id is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getAppVersion()), "app version is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getOs()), "platform is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getClientVersion()), "client version is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDevice()), "device is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDeviceId()), "device id  is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDeviceModel()), "device model is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getOs()), "os is null");
        return true;
    }
}
