package com.sparrow.mvc.interceptor;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.utility.StringUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by harry
 */
public class ValidateMobileClientInformationInterceptor extends SimpleMobileClientInformationInterceptor {
    private Logger logger= LoggerFactory.getLogger(SimpleMobileClientInformationInterceptor.class);

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.preHandle(request, response);
        ClientInformation clientInformation=(ClientInformation) request.getAttribute(CONSTANT.REQUEST_CLIENT_INFORMATION);

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getAppId()),"app id is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getAppVersion()),"app version is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getOs()),"platform is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getClientVersion()),"client version is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDevice()),"device is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDeviceId()),"device id  is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getDeviceModel()),"device model is null");

        Asserts.illegalArgument(StringUtility.isNullOrEmpty(clientInformation.getOs()),"os is null");
        return true;
    }
}
