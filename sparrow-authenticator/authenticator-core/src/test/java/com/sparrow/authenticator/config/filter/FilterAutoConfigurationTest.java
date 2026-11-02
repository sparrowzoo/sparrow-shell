package com.sparrow.authenticator.config.filter;

import com.sparrow.authenticator.AuthenticationInfo;
import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.HostAuthenticationToken;
import com.sparrow.authenticator.config.AuthenticatorConfig;
import com.sparrow.authenticator.filter.MicroServiceFilter;
import com.sparrow.authenticator.filter.MonolithicBearerFilter;
import com.sparrow.protocol.LoginUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterAutoConfigurationTest {
    @Test
    void monolithicBeansUseSpringEnvironmentAndKeepRegistration() throws Exception {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.setEnvironment(new MockEnvironment().withProperty("support_template_engine", "true")
                    .withProperty("login_url", "/configured-login"));
            context.registerBean(AuthenticatorConfigReader.class, AuthenticatorConfig::new);
            context.registerBean(Authenticator.class, StubAuthenticator::new);
            context.register(MonolithicFilterAutoConfiguration.class);
            context.refresh();
            MonolithicBearerFilter filter = context.getBean("monolithicBearerFilter", MonolithicBearerFilter.class);
            FilterRegistrationBean<?> registration = context.getBean("monolithicBearerFilterRegistrationBean", FilterRegistrationBean.class);
            assertSame(filter, registration.getFilter());
            assertEquals(2, registration.getOrder());
            assertEquals(List.of("/*"), List.copyOf(registration.getUrlPatterns()));
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/private");
            request.setServletPath("/private");
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, (req, res) -> fail());
            assertEquals("/configured-login?http://localhost/private", response.getRedirectedUrl());
        }
    }

    @Test
    void microserviceBeansNeedOnlyAuthenticatorConfigAndKeepRegistration() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.registerBean(AuthenticatorConfigReader.class, AuthenticatorConfig::new);
            context.register(MicroServiceFilterAutoConfiguration.class);
            context.refresh();
            MicroServiceFilter filter = context.getBean("microServiceFilter", MicroServiceFilter.class);
            FilterRegistrationBean<?> registration = context.getBean("microServiceFilterRegistrationBean", FilterRegistrationBean.class);
            assertSame(filter, registration.getFilter());
            assertEquals(2, registration.getOrder());
            assertEquals(List.of("/*"), List.copyOf(registration.getUrlPatterns()));
        }
    }

    private static final class StubAuthenticator implements Authenticator {
        @Override
        public String login(AuthenticationInfo info) { throw new UnsupportedOperationException(); }

        @Override
        public LoginUser authenticate(HostAuthenticationToken token) { return new DefaultLoginUser(); }
    }
}
