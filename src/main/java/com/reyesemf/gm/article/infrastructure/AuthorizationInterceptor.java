package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;



import static com.reyesemf.gm.article.domain.model.ActionName.LOGIN;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;


@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String AUTH_TOKEN = "x-auth-token";

    @Autowired
    private AuthenticationService authenticationService;

    private static final Logger LOG = getLogger(AuthorizationInterceptor.class);

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        RequiredAction required = ((HandlerMethod) handler).getMethodAnnotation(RequiredAction.class);

        LOG.info("authentication-service - 1");

        if (isNull(required)) {
            LOG.info("authentication-service - 2");
            throw new InternalAuthenticationServiceException("Unhandled Action");
        }

        LOG.info("authentication-service - 3");

        if (!LOGIN.equals(required.value())) {
            LOG.info("authentication-service - 4");
            authenticationService.validate(request.getHeader(AUTH_TOKEN), required);
        }

        LOG.info("authentication-service - 5");

        return TRUE;

    }


}
