package com.sparrow.facade.authorizing;

import com.sparrow.authorizing.AuthenticatorDemo;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.Authenticator;
import com.sparrow.protocol.LoginToken;

public class Test {

    public static void main(String[] args) {
        ApplicationContext.getContainer().init();
        Authenticator authorizingSupport = new AuthenticatorDemo();
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

        String sign = authorizingSupport.sign(loginToken, "111111");
        System.out.printf(sign);


        sign="aWQ9MSZuYW1lPXpoYW5nc2FuJmxvZ2luPW5pY2stemhhbmdzYW4mZXhwaXJlQXQ9MTY1Njk1MjI2ODE0MiZjZW50PTEwMCZhdmF0YXI9aHR0cDovL2xvY2FsaG9zdCZkZXZpY2VJZD0xMjMuMTE3LjE4Mi4yMTImYWN0aXZhdGU9dHJ1ZSZkYXlzPTIw.tkbXwsprLMBI0S2S06%2BjMQBljJ4%3D";
        loginToken = authorizingSupport.authenticate(sign, "218.247.142.217");
        System.out.printf(loginToken.getUserName());
    }
}
