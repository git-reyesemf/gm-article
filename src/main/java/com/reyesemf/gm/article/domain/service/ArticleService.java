package com.reyesemf.gm.article.domain.service;

import com.reyesemf.gm.article.domain.model.Article;

public interface ArticleService {
    Article createOrUpdate(Article article);
    Article getBySlug(String articleSlug);
    void deleteBySlug(String articleSlug);
}
