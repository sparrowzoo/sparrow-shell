package com.sparrow.authenticator.notifier;

import com.sparrow.authenticator.LoginUser;

public class LoginEvent implements Event<LoginUser> {

    public LoginEvent(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    private LoginUser loginUser;


    @Override
    public EventType getEventType() {
        return AuthcEventType.LOGIN;
    }

    @Override
    public LoginUser getBody() {
        return this.loginUser;
    }
}
