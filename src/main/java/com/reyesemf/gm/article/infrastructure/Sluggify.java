package com.reyesemf.gm.article.infrastructure;

import java.text.Normalizer;
import java.util.function.Function;

import static java.text.Normalizer.normalize;
import static java.util.Objects.requireNonNull;

public class Sluggify implements Function<String, String> {

    @Override
    public String apply(String input) {
        requireNonNull(input, "Input must be not null");
        return normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-+|-+$", "");
    }

}