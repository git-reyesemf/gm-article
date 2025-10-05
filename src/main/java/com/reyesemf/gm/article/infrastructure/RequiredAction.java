package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.model.ActionName;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface RequiredAction {
    ActionName value();
}
