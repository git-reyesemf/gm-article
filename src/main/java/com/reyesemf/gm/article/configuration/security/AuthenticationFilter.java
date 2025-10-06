package com.reyesemf.gm.article.configuration.security;

import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static com.reyesemf.gm.article.domain.model.ActionName.LOGIN;
import static java.util.Objects.isNull;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTH_TOKEN = "x-auth-token";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);

            if (!isNull(handlerChain) && handlerChain.getHandler() instanceof HandlerMethod) {
                RequiredAction required = ((HandlerMethod) handlerChain.getHandler()).getMethodAnnotation(RequiredAction.class);

                if (isNull(required)) {
                    throw new InternalAuthenticationServiceException("Unhandled Action");
                }

                if (!LOGIN.equals(required.value())) {
                    Session session = authenticationService.validate(request.getHeader(AUTH_TOKEN), required);
                    SecurityContextHolder.getContext().setAuthentication(new SessionAuthentication(session));
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
