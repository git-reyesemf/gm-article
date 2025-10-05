package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String AUTH_TOKEN = "x-auth-token";

    @Autowired
    private AuthenticationService authenticationService;

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        RequiredAction required = ((HandlerMethod) handler).getMethodAnnotation(RequiredAction.class);

        if (isNull(required)) {
            throw new InternalAuthenticationServiceException("Unhandled Action");
        }

        authenticationService.validate(request.getHeader(AUTH_TOKEN), required);

        return TRUE;

    }


}
