package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.UserRepository;
import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.model.User;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static com.reyesemf.gm.article.domain.model.ActionName.GET_ALL_CATEGORIES;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Sql(scripts = "classpath:/db/mysql/schema.sql")
@Sql(scripts = "classpath:/db/mysql/data.sql")
public class AuthenticationServiceImplTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Use existing test user from data.sql
        testUser = userRepository.findByUsername("admin_user")
                .orElseThrow(() -> new RuntimeException("Test user not found"));
    }

    @Test
    public void givenValidUserWhenAuthenticateThenReturnSession() {
        // When
        Session session = authenticationService.authenticate(testUser);

        // Then
        assertNotNull(session);
        assertNotNull(session.getToken());
        assertEquals(Session.Status.ACTIVE, session.getStatus());
        assertNotNull(session.getExpiresAt());
        assertEquals(testUser.getId(), session.getUser().getId());
    }

    @Test
    public void givenNullUserWhenAuthenticateThenThrowException() {
        // When & Then
        assertThrows(Exception.class, () -> authenticationService.authenticate(null));
    }

    @Test
    public void givenValidSessionTokenAndActionWhenValidateThenReturnSession() {
        // Given
        Session createdSession = authenticationService.authenticate(testUser);
        String sessionToken = createdSession.getToken();

        // Create RequiredAction mock
        RequiredAction requiredAction = new RequiredAction() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RequiredAction.class;
            }

            @Override
            public com.reyesemf.gm.article.domain.model.ActionName value() {
                return GET_ALL_CATEGORIES;
            }
        };

        // When
        Session validatedSession = authenticationService.validate(sessionToken, requiredAction);

        // Then
        assertNotNull(validatedSession);
        assertEquals(sessionToken, validatedSession.getToken());
        assertEquals(Session.Status.ACTIVE, validatedSession.getStatus());
    }

    @Test
    public void givenInvalidSessionTokenWhenValidateThenThrowSessionAuthenticationException() {
        // Given
        String invalidToken = "invalid-token";
        RequiredAction requiredAction = new RequiredAction() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RequiredAction.class;
            }

            @Override
            public com.reyesemf.gm.article.domain.model.ActionName value() {
                return GET_ALL_CATEGORIES;
            }
        };

        // When & Then
        assertThrows(SessionAuthenticationException.class, 
            () -> authenticationService.validate(invalidToken, requiredAction));
    }

    @Test
    public void givenNullSessionTokenWhenValidateThenThrowSessionAuthenticationException() {
        // Given
        RequiredAction requiredAction = new RequiredAction() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RequiredAction.class;
            }

            @Override
            public com.reyesemf.gm.article.domain.model.ActionName value() {
                return GET_ALL_CATEGORIES;
            }
        };

        // When & Then
        assertThrows(SessionAuthenticationException.class, 
            () -> authenticationService.validate(null, requiredAction));
    }

    @Test
    public void givenValidSessionWhenValidateThenDoesNotThrow() {
        // Given
        Session session = authenticationService.authenticate(testUser);
        String sessionToken = session.getToken();
        
        RequiredAction requiredAction = new RequiredAction() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RequiredAction.class;
            }

            @Override
            public com.reyesemf.gm.article.domain.model.ActionName value() {
                return GET_ALL_CATEGORIES;
            }
        };

        // When & Then
        assertDoesNotThrow(() -> authenticationService.validate(sessionToken, requiredAction));
    }
}
