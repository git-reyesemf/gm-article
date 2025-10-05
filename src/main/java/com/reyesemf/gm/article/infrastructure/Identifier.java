package com.reyesemf.gm.article.infrastructure;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static java.util.UUID.randomUUID;

@Component
public class Identifier implements Supplier<String> {
    
    @Override
    public String get() {
        return randomUUID().toString().replace("-", "").toUpperCase();
    }

}
