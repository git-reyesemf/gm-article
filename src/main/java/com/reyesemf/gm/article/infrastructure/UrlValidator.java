package com.reyesemf.gm.article.infrastructure;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class UrlValidator implements Consumer<String> {
    private static final org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator(new String[]{"http", "https"});

    @Override
    public void accept(String value) {
        requireNonNull(value, "Url must be not null");
        if (!urlValidator.isValid(value)) {
            throw new IllegalArgumentException("Invalid url: " + value);
        }
    }

}
