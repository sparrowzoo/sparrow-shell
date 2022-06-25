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

import com.sparrow.constant.Config;
import com.sparrow.protocol.constant.ClientInformation;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.enums.PLATFORM;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleMobileClientInformationInterceptor implements HandlerInterceptor {
    private ServletUtility servletUtility = ServletUtility.getInstance();

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        com.sparrow.protocol.ClientInformation clientInformation = new com.sparrow.protocol.ClientInformation();
        clientInformation.setIp(servletUtility.getClientIp(request));
        String appId = request.getHeader(ClientInformation.APP_ID);
        if (!StringUtility.isNullOrEmpty(appId)) {
            clientInformation.setAppId(Integer.valueOf(appId));
        }

        String appVersion = request.getHeader(ClientInformation.APP_VERSION);
        if (!StringUtility.isNullOrEmpty(appVersion)) {
            clientInformation.setAppVersion(Float.valueOf(appVersion));
        }

        clientInformation.setBssid(request.getHeader(ClientInformation.BSSID));
        clientInformation.setChannel(request.getHeader(ClientInformation.CHANNEL));
        clientInformation.setClientVersion(request.getHeader(ClientInformation.CLIENT_VERSION));

        clientInformation.setDevice(request.getHeader(ClientInformation.DEVICE));
        clientInformation.setDeviceId(request.getHeader(ClientInformation.DEVICE_ID));
        clientInformation.setDeviceModel(request.getHeader(ClientInformation.DEVICE_MODEL));

        clientInformation.setIdfa(request.getHeader(ClientInformation.IDFA));
        clientInformation.setImei(request.getHeader(ClientInformation.IMEI));
        String latitude = request.getHeader(ClientInformation.LATITUDE);
        if (!StringUtility.isNullOrEmpty(latitude)) {
            clientInformation.setLatitude(Double.valueOf(request.getHeader(ClientInformation.LATITUDE)));
        }

        String longitude = request.getHeader(ClientInformation.LONGITUDE);
        if (!StringUtility.isNullOrEmpty(longitude)) {
            clientInformation.setLongitude(Double.valueOf(request.getHeader(ClientInformation.LONGITUDE)));
        }

        clientInformation.setOs(request.getHeader(ClientInformation.OS));
        clientInformation.setNetwork(request.getHeader(ClientInformation.NETWORK));
        String startTime = request.getHeader(ClientInformation.START_TIME);
        if (!StringUtility.isNullOrEmpty(startTime)) {
            clientInformation.setStartTime(Long.valueOf(startTime));
        }
        String resumeTime = request.getHeader(ClientInformation.RESUME_TIME);

        if (!StringUtility.isNullOrEmpty(resumeTime)) {
            clientInformation.setResumeTime(Long.valueOf(resumeTime));
        }
        clientInformation.setWebsite(rootPath);
        clientInformation.setUserAgent(request.getHeader(ClientInformation.USER_AGENT));
        UserAgent userAgent = UserAgent.parseUserAgentString(clientInformation.getUserAgent());
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        if (os.getDeviceType().equals(DeviceType.COMPUTER)) {
            clientInformation.setOs(os.getGroup().getName());
            clientInformation.setPlatform(PLATFORM.PC);
            clientInformation.setDevice(browser.getName());
            clientInformation.setDeviceId(clientInformation.getIp());
        }

        String simulate = request.getHeader(ClientInformation.SIMULATE);
        if (!StringUtility.isNullOrEmpty(simulate)) {
            clientInformation.setSimulate(Boolean.valueOf(simulate));
        }
        request.setAttribute(Constant.REQUEST_CLIENT_INFORMATION, clientInformation);
        return true;
    }

    @Override public void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
