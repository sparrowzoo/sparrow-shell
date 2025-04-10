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

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.ClientInfoConstant;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.enums.Platform;
import com.sparrow.servlet.HandlerInterceptor;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.support.web.WebConfigReader;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
public class SimpleMobileClientInformationInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(SimpleMobileClientInformationInterceptor.class);
    private ServletUtility servletUtility = ServletUtility.getInstance();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ClientInformation clientInformation = new ClientInformation();
        WebConfigReader configReader = ApplicationContext.getContainer().getBean(WebConfigReader.class);
        String rootPath = configReader.getRootPath();
        clientInformation.setWebsite(rootPath);
        clientInformation.setIp(servletUtility.getClientIp(request));
        String appId = request.getHeader(ClientInfoConstant.APP_ID);
        if (!StringUtility.isNullOrEmpty(appId)) {
            clientInformation.setAppId(Integer.parseInt(appId));
        }

        String appVersion = request.getHeader(ClientInfoConstant.APP_VERSION);
        if (!StringUtility.isNullOrEmpty(appVersion)) {
            clientInformation.setAppVersion(Float.parseFloat(appVersion));
        }

        clientInformation.setSsid(request.getHeader(ClientInfoConstant.SSID));
        clientInformation.setBssid(request.getHeader(ClientInfoConstant.BSSID));
        clientInformation.setChannel(request.getHeader(ClientInfoConstant.CHANNEL));
        clientInformation.setClientVersion(request.getHeader(ClientInfoConstant.CLIENT_VERSION));

        clientInformation.setIdfa(request.getHeader(ClientInfoConstant.IDFA));
        clientInformation.setImei(request.getHeader(ClientInfoConstant.IMEI));

        clientInformation.setDevice(request.getHeader(ClientInfoConstant.DEVICE));
        clientInformation.setDeviceId(clientInformation.getImei());
        clientInformation.setDeviceModel(request.getHeader(ClientInfoConstant.DEVICE_MODEL));


        String latitude = request.getHeader(ClientInfoConstant.LATITUDE);
        if (!StringUtility.isNullOrEmpty(latitude)) {
            clientInformation.setLatitude(Double.parseDouble(request.getHeader(ClientInfoConstant.LATITUDE)));
        }

        String longitude = request.getHeader(ClientInfoConstant.LONGITUDE);
        if (!StringUtility.isNullOrEmpty(longitude)) {
            clientInformation.setLongitude(Double.parseDouble(request.getHeader(ClientInfoConstant.LONGITUDE)));
        }

        clientInformation.setOs(request.getHeader(ClientInfoConstant.OS));
        clientInformation.setNetwork(request.getHeader(ClientInfoConstant.NETWORK));
        String startTime = request.getHeader(ClientInfoConstant.START_TIME);
        if (!StringUtility.isNullOrEmpty(startTime)) {
            clientInformation.setStartTime(Long.parseLong(startTime));
        }
        String resumeTime = request.getHeader(ClientInfoConstant.RESUME_TIME);

        if (!StringUtility.isNullOrEmpty(resumeTime)) {
            clientInformation.setResumeTime(Long.parseLong(resumeTime));
        }
        clientInformation.setUserAgent(request.getHeader(ClientInfoConstant.USER_AGENT));
        UserAgent userAgent = UserAgent.parseUserAgentString(clientInformation.getUserAgent());
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        logger.info("device type {},browser type {}", os.getDeviceType(), browser.getBrowserType());
        if (os.getDeviceType().equals(DeviceType.COMPUTER) || BrowserType.MOBILE_BROWSER.equals(browser.getBrowserType())) {
            clientInformation.setOs(os.getGroup().getName());
            clientInformation.setPlatform(Platform.PC);
            clientInformation.setDevice(browser.getName());
            clientInformation.setDeviceId(clientInformation.getIp());
        }

        String simulate = request.getHeader(ClientInfoConstant.SIMULATE);
        if (!StringUtility.isNullOrEmpty(simulate)) {
            clientInformation.setSimulate(Boolean.valueOf(simulate));
        }
        request.setAttribute(Constant.REQUEST_CLIENT_INFORMATION, clientInformation);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response) {

    }
}
