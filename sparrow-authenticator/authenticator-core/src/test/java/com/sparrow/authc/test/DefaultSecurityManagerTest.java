package com.sparrow.authc.test;

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.DefaultSecurityManager;
import com.sparrow.authenticator.Session;
import com.sparrow.authenticator.SessionManager;
import com.sparrow.authenticator.Signature;
import com.sparrow.authenticator.config.AuthenticatorConfig;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.realm.EmptyRealm;
import com.sparrow.authenticator.session.DefaultSessionParser;
import com.sparrow.authenticator.session.dao.DefaultSession;
import com.sparrow.authenticator.session.dao.EmptySessionDao;
import com.sparrow.authenticator.token.BearerToken;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultSecurityManagerTest {
    private final DefaultLoginUser user = new DefaultLoginUser();
    private final AuthenticatorConfig config = new AuthenticatorConfig();

    @Test
    void acceptsValidSessionWithoutReversingAssertions() throws BusinessException {
        assertSame(user, manager(session()).authenticate(new BearerToken("token", "device")));
    }

    @Test
    void rejectsExpiredSessionWithOriginalError() {
        DefaultSession session = session();
        session.setExpireAt(0L);
        assertError(session, "device", AuthenticatorError.USER_TOKEN_EXPIRED);
    }

    @Test
    void rejectsDeviceMismatchWithOriginalError() {
        assertError(session(), "other-device", AuthenticatorError.USER_DEVICE_NOT_MATCH);
    }

    @ParameterizedTest
    @CsvSource({"-1, USER_KICKOUT", "-2, USER_DEVICE_KICKOUT", "-3, USER_REPLACED", "0, USER_DISABLE"})
    void rejectsInvalidStatusesWithOriginalError(int status, AuthenticatorError error) {
        DefaultSession session = session();
        session.setStatus(status);
        assertError(session, "device", error);
    }

    @Test
    void respectsDisabledHostAndStatusValidation() throws BusinessException {
        config.setValidateHost(false);
        config.setValidateStatus(false);
        DefaultSession session = session();
        session.setStatus(0);
        assertSame(user, manager(session).authenticate(new BearerToken("token", "other-device")));
    }

    private void assertError(DefaultSession session, String host, AuthenticatorError expected) {
        BusinessException error = assertThrows(BusinessException.class,
                () -> manager(session).authenticate(new BearerToken("token", host)));
        assertSame(expected, error.getErrorSupport());
    }

    private DefaultSession session() {
        DefaultSession session = new DefaultSession();
        session.setExpireAt(System.currentTimeMillis() + 600_000);
        session.setStatus(1);
        session.setHost("device");
        return session;
    }

    private DefaultSecurityManager manager(DefaultSession session) {
        config.setRenewal(false);
        Signature signature = new Signature() {
            @Override
            public String sign(LoginUser loginUser, String key) {
                throw new UnsupportedOperationException();
            }

            @Override
            public LoginUser verify(String token, String key) {
                return user;
            }
        };
        SessionManager sessions = new SessionManager() {
            @Override
            public Session start(LoginUser loginUser) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Session getSession(LoginUser loginUser) {
                return session;
            }
        };
        return new DefaultSecurityManager(new DefaultSessionParser(), new EmptyRealm(), signature,
                sessions, new EmptySessionDao(), config);
    }
}
