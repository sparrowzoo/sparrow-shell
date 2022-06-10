package com.sparrow.authorizing;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginToken;
import com.sparrow.support.AbstractAuthorizingService;

public class AuthorizingDemo extends AbstractAuthorizingService {
    @Override
    protected String getSecret(Long userId) {
        //getPasswordByUserId();
        return "111111";
    }

    @Override
    public boolean isAuthorized(LoginToken user, String url, String code) throws BusinessException {
        return true;
    }
}
