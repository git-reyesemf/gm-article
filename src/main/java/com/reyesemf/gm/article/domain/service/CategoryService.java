package com.reyesemf.gm.article.domain.service;

import com.reyesemf.gm.article.domain.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category createOrUpdate(Category category);
    Category getBySlug(String categorySlug);
    void deleteBySlug(String categorySlug);
}
