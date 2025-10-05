package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.datasource.repository.UserRepository;
import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.model.User;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static com.reyesemf.gm.article.domain.model.ActionName.GET_ALL_CATEGORIES;
import static com.reyesemf.gm.article.domain.model.ActionName.LOGIN;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Sql(scripts = "classpath:/db/mysql/schema.sql")
@Sql(scripts = "classpath:/db/mysql/data.sql")
public class AuthorizationInterceptorTest {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Session validSession;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        // Use existing test user from data.sql
        User user = userRepository.findByUsername("admin_user")
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        // Create a valid session for testing
        validSession = authenticationService.authenticate(user);
    }

    @Test
    public void givenValidTokenAndRequiredActionWhenPreHandleThenReturnTrue() throws Exception {
        // Given
        request.addHeader(AuthorizationInterceptor.AUTH_TOKEN, validSession.getToken());
        
        Method method = TestController.class.getMethod("protectedEndpoint");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);

        // When
        boolean result = authorizationInterceptor.preHandle(request, response, handlerMethod);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenInvalidTokenWhenPreHandleThenThrowSessionAuthenticationException() throws Exception {
        // Given
        request.addHeader(AuthorizationInterceptor.AUTH_TOKEN, "invalid-token");
        
        Method method = TestController.class.getMethod("protectedEndpoint");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);

        // When & Then
        assertThrows(SessionAuthenticationException.class, 
            () -> authorizationInterceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    public void givenNoTokenWhenPreHandleThenThrowSessionAuthenticationException() throws Exception {
        // Given - no token header
        Method method = TestController.class.getMethod("protectedEndpoint");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);

        // When & Then
        assertThrows(SessionAuthenticationException.class, 
            () -> authorizationInterceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    public void givenHandlerWithoutRequiredActionWhenPreHandleThenThrowInternalAuthenticationServiceException() throws Exception {
        // Given
        request.addHeader(AuthorizationInterceptor.AUTH_TOKEN, validSession.getToken());
        
        Method method = TestController.class.getMethod("unprotectedEndpoint");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);

        // When & Then
        assertThrows(InternalAuthenticationServiceException.class, 
            () -> authorizationInterceptor.preHandle(request, response, handlerMethod));
    }

    @Test
    public void givenNonHandlerMethodWhenPreHandleThenThrowClassCastException() {
        // Given
        request.addHeader(AuthorizationInterceptor.AUTH_TOKEN, validSession.getToken());
        Object nonHandlerMethod = new Object();

        // When & Then
        assertThrows(ClassCastException.class, 
            () -> authorizationInterceptor.preHandle(request, response, nonHandlerMethod));
    }

    @Test
    public void givenLoginActionWhenPreHandleThenSkipValidationAndReturnTrue() throws Exception {
        // Given - no token header (should normally fail, but LOGIN should be skipped)
        Method method = TestController.class.getMethod("loginEndpoint");
        HandlerMethod handlerMethod = new HandlerMethod(new TestController(), method);

        // When
        boolean result = authorizationInterceptor.preHandle(request, response, handlerMethod);

        // Then
        assertTrue(result);
        // Verify that authenticationService.validate was never called
        // (we can't verify this easily without mocking, but the test passing means it worked)
    }

    // Test controller class for reflection
    public static class TestController {
        
        @RequiredAction(GET_ALL_CATEGORIES)
        public void protectedEndpoint() {}
        
        @RequiredAction(LOGIN)
        public void loginEndpoint() {}
        
        public void unprotectedEndpoint() {}
    }
}
