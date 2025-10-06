package com.reyesemf.gm.article.configuration.security;

import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

import static com.reyesemf.gm.article.domain.model.ActionName.LOGIN;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Filtro de Spring Security que valida autenticación por token.
 * Adaptación del AuthorizationInterceptor original a Spring Security.
 */
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTH_TOKEN = "x-auth-token";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private static final Logger LOG = getLogger(AuthorizationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
            
            if (isNull(handlerChain) || !(handlerChain.getHandler() instanceof HandlerMethod)) {
                filterChain.doFilter(request, response);
                return;
            }

            RequiredAction required = ((HandlerMethod) handlerChain.getHandler()).getMethodAnnotation(RequiredAction.class);

            if (isNull(required)) {
                throw new InternalAuthenticationServiceException("Unhandled Action");
            }

            if (!LOGIN.equals(required.value())) {
                Session session = authenticationService.validate(request.getHeader(AUTH_TOKEN), required);
                
                // Configurar contexto de Spring Security
                SessionAuthentication authentication = new SessionAuthentication(session);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            LOG.error("Authentication error: {}", e.getMessage());
            
            int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            if (e.getMessage() != null && e.getMessage().contains("Forbidden")) {
                statusCode = HttpServletResponse.SC_FORBIDDEN;
            }
            
            response.sendError(statusCode, e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}