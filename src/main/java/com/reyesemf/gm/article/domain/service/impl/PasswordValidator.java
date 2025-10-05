package com.reyesemf.gm.article.domain.service.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Component
public class PasswordValidator implements BiConsumer<String, String> {

    private static final Pattern SHA256_PATTERN = Pattern.compile("^[a-fA-F0-9]{64}$");

    @Override
    public void accept(String pwd1, String pwd2) {
        requireNonNull(pwd1, "Password Must be Not Null");
        requireNonNull(pwd2, "Password Must be Not Null");

        if (!SHA256_PATTERN.matcher(pwd1).matches()) {
            throw new IllegalArgumentException("First password must be a valid SHA-256 hash (64 hexadecimal characters)");
        }

        if (!SHA256_PATTERN.matcher(pwd2).matches()) {
            throw new IllegalArgumentException("Second password must be a valid SHA-256 hash (64 hexadecimal characters)");
        }

        if (!pwd1.equals(pwd2)) {
            throw new BadCredentialsException("Bad credentials.");
        }

    }

}
