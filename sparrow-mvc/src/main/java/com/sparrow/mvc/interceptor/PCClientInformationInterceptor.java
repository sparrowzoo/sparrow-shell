package com.sparrow.mvc.interceptor;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.enums.PLATFORM;
import com.sparrow.utility.StringUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by harry
 */
public class PCClientInformationInterceptor extends SimpleMobileClientInformationInterceptor {
    private Logger logger= LoggerFactory.getLogger(SimpleMobileClientInformationInterceptor.class);

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.preHandle(request, response);
        ClientInformation clientInformation=(ClientInformation) request.getAttribute(CONSTANT.REQUEST_CLIENT_INFORMATION);
        clientInformation.setPlatform(PLATFORM.PC);
        String userAgent = clientInformation.getUserAgent();

        //todo parse user agent for browser
        return true;
    }
}
