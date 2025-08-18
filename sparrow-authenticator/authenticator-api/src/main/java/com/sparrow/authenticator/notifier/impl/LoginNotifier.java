package com.sparrow.authenticator.notifier.impl;

import com.sparrow.authenticator.LoginUser;
import com.sparrow.authenticator.notifier.*;
import com.sparrow.protocol.BusinessException;

public class LoginNotifier extends AbstractNotifier<LoginUser> {

    @Override
    public String getType() {
        return AuthcEventType.LOGIN.name();
    }

    @Override
    public void notify(Event<LoginUser> event) throws BusinessException {

    }
}
