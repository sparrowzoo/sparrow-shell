package com.sparrow.mvc.interceptor;

import com.sparrow.constant.Config;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.CLIENT_INFORMATION;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.enums.PLATFORM;
import com.sparrow.mvc.HandlerInterceptor;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by harry
 */
public class SimpleMobileClientInformationInterceptor implements HandlerInterceptor {
    private ServletUtility servletUtility=ServletUtility.getInstance();
    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        ClientInformation clientInformation = new ClientInformation();
        clientInformation.setIp(servletUtility.getClientIp(request));
        String appId = request.getHeader(CLIENT_INFORMATION.APP_ID);
        if (!StringUtility.isNullOrEmpty(appId)) {
            clientInformation.setAppId(Integer.valueOf(appId));
        }

        String appVersion = request.getHeader(CLIENT_INFORMATION.APP_VERSION);
        if (!StringUtility.isNullOrEmpty(appVersion)) {
            clientInformation.setAppVersion(Float.valueOf(appVersion));
        }


        clientInformation.setBssid(request.getHeader(CLIENT_INFORMATION.BSSID));
        clientInformation.setChannel(request.getHeader(CLIENT_INFORMATION.CHANNEL));
        clientInformation.setClientVersion(request.getHeader(CLIENT_INFORMATION.CLIENT_VERSION));

        clientInformation.setDevice(request.getHeader(CLIENT_INFORMATION.DEVICE));
        clientInformation.setDeviceId(request.getHeader(CLIENT_INFORMATION.DEVICE_ID));
        clientInformation.setDeviceModel(request.getHeader(CLIENT_INFORMATION.DEVICE_MODEL));


        clientInformation.setIdfa(request.getHeader(CLIENT_INFORMATION.IDFA));
        clientInformation.setImei(request.getHeader(CLIENT_INFORMATION.IMEI));
        String latitude = request.getHeader(CLIENT_INFORMATION.LATITUDE);
        if (!StringUtility.isNullOrEmpty(latitude)) {
            clientInformation.setLatitude(Double.valueOf(request.getHeader(CLIENT_INFORMATION.LATITUDE)));
        }

        String longitude = request.getHeader(CLIENT_INFORMATION.LONGITUDE);
        if (!StringUtility.isNullOrEmpty(longitude)) {
            clientInformation.setLongitude(Double.valueOf(request.getHeader(CLIENT_INFORMATION.LONGITUDE)));
        }

        clientInformation.setOs(request.getHeader(CLIENT_INFORMATION.OS));
        clientInformation.setNetwork(request.getHeader(CLIENT_INFORMATION.NETWORK));
        String startTime = request.getHeader(CLIENT_INFORMATION.START_TIME);
        if (!StringUtility.isNullOrEmpty(startTime)) {
            clientInformation.setStartTime(Long.valueOf(startTime));
        }
        String resumeTime = request.getHeader(CLIENT_INFORMATION.RESUME_TIME);

        if (!StringUtility.isNullOrEmpty(resumeTime)) {
            clientInformation.setResumeTime(Long.valueOf(resumeTime));
        }
        clientInformation.setWebsite(rootPath);
        clientInformation.setUserAgent(request.getHeader(CLIENT_INFORMATION.USER_AGENT));
        UserAgent userAgent = UserAgent.parseUserAgentString(clientInformation.getUserAgent());
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser= userAgent.getBrowser();
        if(os.getDeviceType().equals(DeviceType.COMPUTER)){
            clientInformation.setOs(os.getGroup().getName());
            clientInformation.setPlatform(PLATFORM.PC);
            clientInformation.setDevice(browser.getName());
            clientInformation.setDeviceId(clientInformation.getIp());
        }


        String simulate=request.getHeader(CLIENT_INFORMATION.SIMULATE);
        if(!StringUtility.isNullOrEmpty(simulate)) {
            clientInformation.setSimulate(Boolean.valueOf(simulate));
        }
        request.setAttribute(CONSTANT.REQUEST_CLIENT_INFORMATION, clientInformation);
        return true;
    }

    @Override public void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}
