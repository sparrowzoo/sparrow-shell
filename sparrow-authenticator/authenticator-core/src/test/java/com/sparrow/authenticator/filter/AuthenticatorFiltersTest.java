package com.sparrow.authenticator.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sparrow.authenticator.AuthenticationInfo;
import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.HostAuthenticationToken;
import com.sparrow.authenticator.config.AuthenticatorConfig;
import com.sparrow.protocol.context.SessionContext;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.authenticator.resolvers.LoginUserArgumentResolver;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticatorFiltersTest {
    @AfterEach
    void clearContext() {
        SessionContext.clearToken();
        SessionContext.clearClient();
    }

    @Test
    void headerWinsAndResolverReceivesBoundUserWithoutUpstreamClientFilter() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        MonolithicBearerFilter filter = monolithic(authenticator, new MockEnvironment());
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "header-token");
        request.addHeader("DEVICE_ID", "browser-device");
        request.setCookies(new Cookie("Authorization", "cookie-token"));
        MethodParameter parameter = new MethodParameter(getClass().getDeclaredMethod("controller", LoginUser.class), 0);
        LoginUserArgumentResolver resolver = new LoginUserArgumentResolver();

        filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> {
            assertTrue(resolver.supportsParameter(parameter));
            assertSame(authenticator.user, assertDoesNotThrow(() -> resolver.resolveArgument(parameter, null, null, null)));
        });

        assertEquals("header-token", authenticator.received.getCredential());
        assertEquals("browser-device", authenticator.received.getHost());
        assertNull(SessionContext.getLoginUser());
        assertNull(SessionContext.getClientInfo());
    }

    @Test
    void nullStringHeaderFallsBackToDecodedCaseInsensitiveCookieAndRemoteAddress() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", " null ");
        request.addHeader("X-Forwarded-For", "203.0.113.99");
        request.addHeader("X-Real-IP", "203.0.113.98");
        request.setRemoteAddr("192.0.2.42");
        request.setCookies(new Cookie("authorization", "cookie%2Btoken%20value"));

        monolithic(authenticator, new MockEnvironment()).doFilter(request, new MockHttpServletResponse(), (req, res) -> {});

        assertEquals("cookie+token value", authenticator.received.getCredential());
        assertEquals("192.0.2.42", authenticator.received.getHost());
        assertNull(SessionContext.getLoginUser());
    }

    @Test
    void upstreamClientDeviceWinsAndNestedLoginUserIsRestoredAfterSwallowedException() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        DefaultLoginUser outer = new DefaultLoginUser();
        ClientInformation client = new ClientInformation();
        client.setDeviceId("upstream-device");
        SessionContext.bindLoginUser(outer);
        SessionContext.bindClientInfo(client);
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "token");
        request.addHeader("DEVICE_ID", "header-device");

        assertDoesNotThrow(() -> monolithic(authenticator, new MockEnvironment()).doFilter(request,
                new MockHttpServletResponse(), (req, res) -> {
                    assertSame(authenticator.user, SessionContext.getLoginUser());
                    throw new IOException("downstream failure");
                }));

        assertEquals("upstream-device", authenticator.received.getHost());
        assertSame(outer, SessionContext.getLoginUser());
        assertSame(client, SessionContext.getClientInfo());
    }

    @Test
    void noTokenReturnsAjax401WithExistingFailureJson() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        MockHttpServletResponse response = new MockHttpServletResponse();
        monolithic(authenticator, new MockEnvironment()).doFilter(request("/private"), response,
                (req, res) -> fail("unauthenticated request reached application"));

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());
        JSONObject json = JSON.parseObject(response.getContentAsString());
        assertEquals(AuthenticatorError.USER_NOT_LOGIN.getCode(), json.getString("code"));
        assertEquals("user_not_login", json.getString("key"));
        assertNull(authenticator.received);
    }

    @Test
    void authenticationFailurePreservesBusinessErrorAndDoesNotRunChain() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        authenticator.error = new BusinessException(AuthenticatorError.USER_TOKEN_ERROR);
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "bad-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        monolithic(authenticator, new MockEnvironment()).doFilter(request, response,
                (req, res) -> fail("invalid token reached application"));

        assertEquals(401, response.getStatus());
        assertEquals(AuthenticatorError.USER_TOKEN_ERROR.getCode(),
                JSON.parseObject(response.getContentAsString()).getString("code"));
        assertNull(SessionContext.getLoginUser());
    }

    @Test
    void htmlRequestRedirectsUsingRefererAndOriginalQuery() throws Exception {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("support_template_engine", "true")
                .withProperty("login_url", "https://passport.example/login");
        MockHttpServletRequest request = request("/save.do");
        request.addHeader("Referer", "https://app.example/form");
        request.setQueryString("page=2");
        MockHttpServletResponse response = new MockHttpServletResponse();

        monolithic(new RecordingAuthenticator(), environment).doFilter(request, response,
                (req, res) -> fail("unauthenticated request reached application"));

        assertEquals("https://passport.example/login?https://app.example/form?page=2", response.getRedirectedUrl());
    }

    @Test
    void ajaxPatternsUseWholeRegexMatchAndOverrideAjaxHeader() throws Exception {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("support_template_engine", "true")
                .withProperty("login_url", "/login")
                .withProperty("ajax_patterns", "/api/.*,/rpc/.*");
        MonolithicBearerFilter filter = monolithic(new RecordingAuthenticator(), environment);
        MockHttpServletResponse ajax = new MockHttpServletResponse();
        filter.doFilter(request("/api/orders"), ajax, (req, res) -> fail());
        assertEquals(401, ajax.getStatus());

        MockHttpServletRequest htmlRequest = request("/prefix/api/orders");
        htmlRequest.addHeader(Constant.IS_AJAX, "true");
        MockHttpServletResponse html = new MockHttpServletResponse();
        filter.doFilter(htmlRequest, html, (req, res) -> fail());
        assertEquals("/login?http://localhost/prefix/api/orders", html.getRedirectedUrl());
    }

    @Test
    void templateModeHonorsAjaxHeaderWhenNoPatternsAreConfigured() throws Exception {
        MonolithicBearerFilter filter = monolithic(new RecordingAuthenticator(), new MockEnvironment()
                .withProperty("support_template_engine", "true").withProperty("login_url", "/login"));
        MockHttpServletRequest request = request("/private");
        request.addHeader(Constant.IS_AJAX, "TrUe");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, (req, res) -> fail());
        assertEquals(401, response.getStatus());
    }

    @Test
    void exclusionWildcardSkipsAuthenticationWithoutPartialPathMatches() throws Exception {
        RecordingAuthenticator authenticator = new RecordingAuthenticator();
        AuthenticatorConfig config = new AuthenticatorConfig();
        config.setExcludePatterns(Set.of("/public/*"));
        MonolithicBearerFilter filter = new MonolithicBearerFilter(authenticator, config, new MockEnvironment());
        AtomicBoolean called = new AtomicBoolean();
        filter.doFilter(request("/public/icon.png"), new MockHttpServletResponse(), (req, res) -> called.set(true));
        assertTrue(called.get());
        assertNull(authenticator.received);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request("/prefix/public/icon.png"), response, (req, res) -> fail());
        assertEquals(401, response.getStatus());
    }

    @Test
    void filterInitAddsWildcardExclusions() throws Exception {
        MonolithicBearerFilter filter = monolithic(new RecordingAuthenticator(), new MockEnvironment());
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter("excludePatterns", "/health*,/assets/*");
        filter.init(config);
        AtomicBoolean called = new AtomicBoolean();
        filter.doFilter(request("/health/live"), new MockHttpServletResponse(), (req, res) -> called.set(true));
        assertTrue(called.get());
    }

    @Test
    void microserviceBindsJsonUserAndRestoresOuterContextOnSuccess() throws Exception {
        DefaultLoginUser outer = new DefaultLoginUser();
        SessionContext.bindLoginUser(outer);
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "{\"userId\":42,\"userName\":\"alice\",\"deviceType\":\"PC\"}");
        MicroServiceFilter filter = new MicroServiceFilter(new AuthenticatorConfig());

        filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> {
            assertEquals(42L, SessionContext.getLoginUser().getUserId());
            assertEquals("alice", SessionContext.getLoginUser().getUserName());
        });

        assertSame(outer, SessionContext.getLoginUser());
    }

    @Test
    void microservicePropagatesDownstreamExceptionAndClearsItsLoginUser() {
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "{\"userId\":42}");
        MicroServiceFilter filter = new MicroServiceFilter(new AuthenticatorConfig());
        IOException failure = new IOException("downstream failure");

        IOException thrown = assertThrows(IOException.class, () -> filter.doFilter(request,
                new MockHttpServletResponse(), (req, res) -> { throw failure; }));

        assertSame(failure, thrown);
        assertNull(SessionContext.getLoginUser());
    }

    @Test
    void microserviceRestoresOuterUserWhenDownstreamFails() {
        DefaultLoginUser outer = new DefaultLoginUser();
        SessionContext.bindLoginUser(outer);
        MockHttpServletRequest request = request("/private");
        request.addHeader("Authorization", "{\"userId\":42}");
        assertThrows(ServletException.class, () -> new MicroServiceFilter(new AuthenticatorConfig())
                .doFilter(request, new MockHttpServletResponse(), (req, res) -> { throw new ServletException("failed"); }));
        assertSame(outer, SessionContext.getLoginUser());
    }

    @Test
    void microserviceExcludedRequestRetainsOuterUserAndSkipsMalformedJson() throws Exception {
        DefaultLoginUser outer = new DefaultLoginUser();
        SessionContext.bindLoginUser(outer);
        AuthenticatorConfig config = new AuthenticatorConfig();
        config.setExcludePatterns(Set.of("/public/*"));
        MockHttpServletRequest request = request("/public/page");
        request.addHeader("Authorization", "bad-json");
        new MicroServiceFilter(config).doFilter(request, new MockHttpServletResponse(),
                (req, res) -> assertSame(outer, SessionContext.getLoginUser()));
        assertSame(outer, SessionContext.getLoginUser());
    }

    private MonolithicBearerFilter monolithic(RecordingAuthenticator authenticator, MockEnvironment environment) {
        return new MonolithicBearerFilter(authenticator, new AuthenticatorConfig(), environment);
    }

    private MockHttpServletRequest request(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        request.setServletPath(path);
        return request;
    }

    private void controller(LoginUser user) {}

    private static final class RecordingAuthenticator implements Authenticator {
        private final DefaultLoginUser user = new DefaultLoginUser();
        private HostAuthenticationToken received;
        private BusinessException error;

        @Override
        public String login(AuthenticationInfo authenticationInfo) { throw new UnsupportedOperationException(); }

        @Override
        public LoginUser authenticate(HostAuthenticationToken token) throws BusinessException {
            received = token;
            if (error != null) {
                throw error;
            }
            return user;
        }
    }
}
