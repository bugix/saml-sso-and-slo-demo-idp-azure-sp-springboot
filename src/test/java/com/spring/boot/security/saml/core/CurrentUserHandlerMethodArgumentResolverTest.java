package com.spring.boot.security.saml.core;

import com.spring.boot.security.saml.CommonTestSupport;
import com.spring.boot.security.saml.TestConfig;
import com.spring.boot.security.saml.stereotypes.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = TestConfig.class)
public class CurrentUserHandlerMethodArgumentResolverTest extends CommonTestSupport {

    @Autowired
    private CurrentUserHandlerMethodArgumentResolver resolver;

    private MethodParameter validParam;

    private MethodParameter notAnnotatedParam;

    private MethodParameter wrongTypeParam;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        validParam = new MethodParameter(
                MethodSamples.class.getMethod("validUser", User.class), 0);
        notAnnotatedParam = new MethodParameter(
                MethodSamples.class.getMethod("notAnnotatedUser", User.class), 0);
        wrongTypeParam = new MethodParameter(
                MethodSamples.class.getMethod("wrongTypeUser", Object.class), 0);
    }

    @Test
    public void testSupportsParameter() {
        assertTrue(resolver.supportsParameter(validParam));
        assertFalse(resolver.supportsParameter(notAnnotatedParam));
        assertFalse(resolver.supportsParameter(wrongTypeParam));
    }

    @Test
    public void testResolveArgument() {
        // given
        ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        WebDataBinderFactory binderFactory = mock(WebDataBinderFactory.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        User stubUser = new User(USER_NAME, "", Collections.emptyList());
        Principal stubPrincipal = new UsernamePasswordAuthenticationToken(stubUser, null);
        when(webRequest.getUserPrincipal()).thenReturn(stubPrincipal);

        // when/then
        assertEquals(stubUser,
                resolver.resolveArgument(validParam, mavContainer, webRequest, binderFactory));
        assertEquals(WebArgumentResolver.UNRESOLVED,
                resolver.resolveArgument(notAnnotatedParam, mavContainer, webRequest, binderFactory));
        assertEquals(WebArgumentResolver.UNRESOLVED,
                resolver.resolveArgument(wrongTypeParam, mavContainer, webRequest, binderFactory));
    }

    @SuppressWarnings("unused")
    private static final class MethodSamples {

        public void validUser(@CurrentUser User user) {
        }

        public void notAnnotatedUser(User user) {
        }

        public void wrongTypeUser(@CurrentUser Object user) {
        }
    }
}
