
package com.sparrow.demo;

import com.sparrow.constant.User;
import com.sparrow.mvc.RequestParameters;
import com.sparrow.mvc.ViewWithModel;
import com.sparrow.param.ParamDemo;
import com.sparrow.protocol.*;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.Authenticator;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.vo.HelloVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    private Authenticator authenticator;
    private ServletContainer servletContainer;

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void setServletContainer(ServletContainer servletContainer) {
        this.servletContainer = servletContainer;
    }

    @RequestParameters("seconds")
    public void timeout(Integer seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

    @RequestParameters("seconds")
    public ViewWithModel error(Integer seconds) throws Throwable {
        if (seconds != null && seconds > 0) {
            Thread.sleep(seconds * 1000);
        }
        throw new NotTryException("error");
    }

    @RequestParameters("key")
    public HelloVO env(String key) throws BusinessException {
        return new HelloVO(System.getenv(key));
    }

    @RequestParameters("key")
    public HelloVO env2(String key) {
        return new HelloVO(key);
    }

    public ViewWithModel hello() {
        logger.info("hello");
        return ViewWithModel.forward("hello", new HelloVO("我来自遥远的sparrow 星球,累死我了..."));
    }

    public ViewWithModel sparrowJspTest() {
        logger.info("sparrow jsp");
        HelloVO hello = new HelloVO("我来自遥远的sparrow 星球,累死我了...");
        return ViewWithModel.forward("sparrow_jsp", hello);
    }

    public ViewWithModel exception() throws BusinessException {
        throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
    }

    public ViewWithModel fly() {
        return ViewWithModel.redirect("fly-here", new HelloVO("我是从fly.jsp飞过来，你是不是没感觉？"));
    }

    public ViewWithModel transit() throws BusinessException {
        return ViewWithModel.transit(new HelloVO("我从你这歇一会，最终我要到transit-here"));
    }

    @RequestParameters("threadId,pageIndex")
    public ViewWithModel thread(Long threadId, Integer pageIndex) {
        return ViewWithModel.forward("thread", new HelloVO("服务器的threadId=" + threadId + ",pageIndex=" + pageIndex));
    }

    public HelloVO json() {
        return new HelloVO("够意思吧，json不用页面");
    }

    public HelloVO postJson(ParamDemo paramDemo) {
        return new HelloVO(paramDemo.getName() + "-" + paramDemo.getAge());
    }

    public ViewWithModel welcome() {
        return ViewWithModel.forward(new HelloVO("jsp page content from server ..."));
    }

    public ViewWithModel login(HttpServletRequest request) throws BusinessException {
        LoginUser loginToken = new LoginUser();
        loginToken.setNickName("nick-zhangsan");
        loginToken.setAvatar("http://localhost");
        ServletUtility servletUtility = ServletUtility.getInstance();
        loginToken.setDeviceId(servletUtility.getDeviceId(request));
        logger.debug("login device id {}", loginToken.getDeviceId());
        loginToken.setExpireAt(System.currentTimeMillis() + 1000 * 60 * 60);
        loginToken.setDays(20);
        loginToken.setUserId(1L);
        loginToken.setUserName("zhangsan");
        String sign = authenticator.sign(loginToken, new LoginUserStatus(1, System.currentTimeMillis() + 100000000000L));
        servletContainer.cookie(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN, sign, 6);
        Result result = new Result<>(loginToken, "login_success");
        return ViewWithModel.transit("/login_success", "/", result);
    }

    public ViewWithModel authorizing() {
        return ViewWithModel.forward(new HelloVO("你成功了"));
    }
}
