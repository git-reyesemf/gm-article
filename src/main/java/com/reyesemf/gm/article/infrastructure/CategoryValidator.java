package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@Service
public class CategoryValidator implements Consumer<Category> {
    private final Consumer<String> ssrfValidator = new SSRFValidator();
    private final Consumer<String> urlValidator = new UrlValidator();

    @Override
    public void accept(Category category) {
        requireNonNull(category, "Category must be not null");
        ssrfValidator.accept(category.getName());
        ssrfValidator.accept(category.getDescription());
        urlValidator.accept(category.getImage());
        urlValidator.accept(category.getUrl());
    }

}