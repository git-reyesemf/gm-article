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

/**
 * Filtro de autenticación basado en tokens para Spring Security.
 * 
 * <p>Este filtro intercepta todas las peticiones HTTP a endpoints anotados con {@link RequiredAction}
 * y valida el token de sesión enviado en el header 'x-auth-token'.</p>
 * 
 * <p><strong>Flujo de autenticación:</strong></p>
 * <ol>
 *   <li>Lee el header 'x-auth-token' del request</li>
 *   <li>Valida el token usando {@link AuthenticationService#validate}</li>
 *   <li>Verifica que el usuario tiene permisos para la acción requerida</li>
 *   <li>Configura el {@link SecurityContextHolder} con {@link SessionAuthentication}</li>
 * </ol>
 * 
 * <p><strong>Endpoints públicos:</strong></p>
 * <ul>
 *   <li>/api/authentication - No requiere token (LOGIN)</li>
 *   <li>/h2-console/** - Consola H2 (solo desarrollo)</li>
 *   <li>/swagger-ui/** - Documentación Swagger</li>
 * </ul>
 * 
 * <p><strong>Endpoints protegidos:</strong></p>
 * <p>Todos los demás endpoints bajo /api/** requieren el header 'x-auth-token' con un token válido.</p>
 * 
 * <p><strong>Ejemplo de uso:</strong></p>
 * <pre>
 * curl -H "x-auth-token: CDF4F9739EA84A7F9572..." http://localhost:8080/api/category
 * </pre>
 * 
 * @see AuthenticationService
 * @see SessionAuthentication
 * @see RequiredAction
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    /**
     * Nombre del header HTTP que contiene el token de autenticación.
     * Valor: "x-auth-token"
     */
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
