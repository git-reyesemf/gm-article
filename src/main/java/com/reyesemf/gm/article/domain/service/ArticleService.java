package com.reyesemf.gm.article.domain.service;

import com.reyesemf.gm.article.domain.model.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getAllByCategorySlug(String categorySlug);
    Article createOrUpdate(Article article);
    Article getBySlug(String articleSlug);
    void deleteBySlug(String articleSlug);
}
