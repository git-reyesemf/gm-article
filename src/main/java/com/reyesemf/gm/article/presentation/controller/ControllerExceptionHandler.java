package com.reyesemf.gm.article.presentation.controller;


import com.reyesemf.gm.article.infrastructure.ResponseBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = getLogger(ControllerExceptionHandler.class);
    private static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Error";
    private static final String NOT_FOUND_TITLE = "Entity Not found Error";
    private static final String UNAUTHORIZED_TITLE = "Unhautorized";

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error(e.getMessage(), e);
        Map<String, Object> errors = createErrors(NOT_FOUND.value(), NOT_FOUND_TITLE, e.getMessage());
        return status(NOT_FOUND.value()).body(errors);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        logger.error(e.getMessage(), e);
        Map<String, Object> errors = createErrors(UNAUTHORIZED.value(), UNAUTHORIZED_TITLE, e.getMessage());
        return status(UNAUTHORIZED.value()).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        Map<String, Object> errors = createErrors(BAD_REQUEST.value(), "Bad Request", e.getMessage());
        return status(BAD_REQUEST.value()).body(errors);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Map<String, Object>> handleException(Exception e) {
        ResponseEntity<Map<String, Object>> response;
        logger.error(e.getMessage(), e);
        Map<String, Object> errors = createErrors(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR_TITLE, e.getMessage());
        response = status(INTERNAL_SERVER_ERROR.value()).body(errors);
        return response;
    }

    private Map<String, Object> createErrors(int status, String title, String detail) {
        ResponseBuilder builder = new ResponseBuilder();
        ResponseBuilder.ErrorObject entry = new ResponseBuilder.ErrorObject(title, detail, status);
        builder.errors(entry);
        return builder.build();
    }

}