package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.model.Article;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class ArticleValidator implements Consumer<Article> {
    private final Consumer<String> ssrfValidator = new SSRFValidator();
    private final Consumer<String> urlValidator = new UrlValidator();

    @Override
    public void accept(Article article) {
        requireNonNull(article, "Article must be not null");
        requireNonNull(article.getName(), "Article.name must be not null");
        requireNonNull(article.getDescription(), "Article.description must be not null");
        requireNonNull(article.getImage(), "Article.image must be not null");
        requireNonNull(article.getUrl(), "Article.url must be not null");
        requireNonNull(article.getCategory(), "Article.category must be not null");

        ssrfValidator.accept(article.getName());
        ssrfValidator.accept(article.getDescription());
        urlValidator.accept(article.getImage());
        urlValidator.accept(article.getUrl());
    }
}