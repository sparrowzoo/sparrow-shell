package com.sparrow.facade.authorizing;

import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.AuthorizingSupport;
import com.sparrow.protocol.LoginToken;

public class Test {

    public static void main(String[] args) {
        ApplicationContext.getContainer().init();
        AuthorizingSupport authorizingSupport = new AuthorizingDemo();
        LoginToken loginToken = new LoginToken();
        loginToken.setNickName("nick-zhangsan");
        loginToken.setAvatar("http://localhost");
        loginToken.setDeviceId("192.168.1.1");
        loginToken.setCent(100L);
        loginToken.setExpireAt(System.currentTimeMillis() + 100000);
        loginToken.setDays(20);
        loginToken.setUserId(1L);
        loginToken.setUserName("zhangsan");
        loginToken.setActivate(true);

        String sign = authorizingSupport.sign(loginToken);
        System.out.printf(sign);

        loginToken= authorizingSupport.authenticate(sign,"192.168.1.1");
        System.out.printf(loginToken.getUserName());
    }
}
