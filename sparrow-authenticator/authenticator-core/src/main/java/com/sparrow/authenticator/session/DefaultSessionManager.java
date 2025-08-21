package com.sparrow.authenticator.session;

import com.sparrow.authenticator.*;
import com.sparrow.authenticator.enums.UserStatus;
import com.sparrow.authenticator.session.dao.DefaultSession;
import com.sparrow.protocol.LoginUser;

public class DefaultSessionManager implements SessionManager {

    private SessionDao sessionDao;

    public DefaultSessionManager(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public Session start(LoginUser loginUser) {
        DefaultSession session =new DefaultSession();
        String sessionKey = String.format("%s:%s:%s",loginUser.getUserId(),loginUser.getDeviceType(),loginUser.getHost());
        session.setSessionKey(sessionKey);
        session.setHost(loginUser.getHost());
        session.setCategory(loginUser.getCategory());
        session.setDeviceType(loginUser.getDeviceType());
        session.setExpireAt(loginUser.getExpireAt());
        session.setStartTime(System.currentTimeMillis());
        session.setStatus(UserStatus.NORMAL.getIdentity());
        session.setLastAccessTime(System.currentTimeMillis());
        return session;
    }

    @Override
    public SessionStatus getSession(SessionKey key) {
        return this.sessionDao.get(key);
    }
}
