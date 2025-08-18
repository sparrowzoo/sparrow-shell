package com.sparrow.authenticator.notifier;

import com.sparrow.protocol.BusinessException;

public interface Notifier<T> {
    String getType();
    void notify(Event<T> event) throws BusinessException;
}
