package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.SessionRepository;
import com.reyesemf.gm.article.datasource.repository.UserRepository;
import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.model.User;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.reyesemf.gm.article.domain.model.Session.Status.ACTIVE;
import static java.time.LocalDateTime.now;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Supplier<String> identifier;

    @Autowired
    private BiConsumer<String, String> passwordValidator;

    @Override
    @Transactional
    public Session authenticate(User candidate) {
        User user = userRepository.findByUsername(candidate.getUsername())
                .orElseThrow(() -> new AuthenticationException("Unknown User") {
                });

        passwordValidator.accept(user.getPassword(), candidate.getPassword());

        Session session = new Session();
        session.setStatus(ACTIVE);
        session.setExpiresAt(now().plusHours(1));
        session.setUser(user);
        session.setToken(identifier.get());

        return sessionRepository.save(session);

    }

    @Transactional
    public Session validate(String sessionToken, RequiredAction required) {
        Session session = sessionRepository.findByToken(sessionToken)
                .orElseThrow(() -> new SessionAuthenticationException("Invalid session sessionToken: " + sessionToken));

        if (!session.isAvailable()) {
            throw new CredentialsExpiredException("Non Available session sessionToken: " + sessionToken);
        }

        if (!session.getUser().hasAction(required)) {
            throw new AccessDeniedException("Forbidden action");
        }

        session.setExpiresAt(now().plusHours(1));

        return session;

    }

}
