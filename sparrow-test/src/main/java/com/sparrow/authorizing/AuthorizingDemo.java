package com.sparrow.authorizing;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.support.AbstractAuthenticatorService;

public class AuthorizingDemo extends AbstractAuthenticatorService {

    @Override
    protected String getEncryptKey() {
        return null;
    }

    @Override
    protected String getDecryptKey() {
        return null;
    }

    @Override
    protected boolean validateDeviceId() {
        return false;
    }

    @Override
    protected boolean validateStatus() {
        return false;
    }

    @Override
    protected String sign(LoginUser loginUser, String s) {
        return null;
    }

    @Override
    protected LoginUser verify(String s, String s1) throws BusinessException {
        return null;
    }

    @Override
    protected void setUserStatus(Long aLong, LoginUserStatus loginUserStatus) {

    }

    @Override
    protected LoginUserStatus getUserStatus(Long aLong) {
        return null;
    }

    @Override
    protected LoginUserStatus getUserStatusFromDB(Long aLong) {
        return null;
    }

    @Override
    protected void renewal(Long aLong, LoginUserStatus loginUserStatus) {

    }
}
