package com.reyesemf.gm.article.presentation.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter implements Filter {

    public static final String AUTHORIZATION_HEADER = "X-Client-Id";
    private static final String AUTHORIZATION_SECRET_IDENTIFIER = "SECRET_X_CLIENT_";
    private static final String ERROR_MESSAGE = "{\"errors\": [{\"status\": \"403\", \"title\": \"Unauthorized access\", \"detail\": \"Invalid X-Client-Id header\"}]}";
    private static Map<String, String> clientIds;

    public AuthorizationFilter() {
    }

    public AuthorizationFilter(Map<String, String> environment) {
        clientIds = setClientIds(environment);
    }

    public static void setClientIdsFromEnvironment(Map<String, String> environment) {
        AuthorizationFilter.clientIds = setClientIds(environment);
    }

    private static Map<String, String> setClientIds(Map<String, String> environment) {
        Map<String, String> clients = new HashMap<>();
        for (String envName : environment.keySet()) {
            if (envName.contains(AUTHORIZATION_SECRET_IDENTIFIER)) {
                clients.put(environment.get(envName), envName.replace(AUTHORIZATION_SECRET_IDENTIFIER, ""));
            }
        }
        return clients;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (nonNull(header) && nonNull(clientIds.get(header))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            error(servletResponse);
        }
    }

    private void error(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.reset();
        response.setContentLength(ERROR_MESSAGE.length());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(SC_UNAUTHORIZED);
        response.getWriter().write(ERROR_MESSAGE);
    }

}


