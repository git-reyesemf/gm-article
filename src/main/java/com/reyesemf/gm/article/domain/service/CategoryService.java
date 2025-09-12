package com.reyesemf.gm.article.domain.service;

import com.reyesemf.gm.article.domain.model.Category;

public interface CategoryService {
    Category createOrUpdate(Category category);
    Category getBySlug(String categorySlug);
    void deleteBySlug(String categorySlug);
}
