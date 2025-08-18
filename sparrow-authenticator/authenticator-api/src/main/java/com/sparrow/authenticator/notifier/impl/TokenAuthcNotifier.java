package com.sparrow.authenticator.notifier.impl;

import com.sparrow.authenticator.notifier.AbstractNotifier;
import com.sparrow.authenticator.notifier.AuthcEventType;
import com.sparrow.authenticator.notifier.Event;
import com.sparrow.protocol.BusinessException;

public class TokenAuthcNotifier extends AbstractNotifier<String> {

    @Override
    public String getType() {
        return AuthcEventType.AUTHC.name();
    }

    @Override
    public void notify(Event<String> event) throws BusinessException {
       //todo
    }

}
