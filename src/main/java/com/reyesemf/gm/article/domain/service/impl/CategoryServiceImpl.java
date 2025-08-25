package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.CategoryRepository;
import com.reyesemf.gm.article.domain.model.Category;
import com.reyesemf.gm.article.domain.service.CategoryService;
import com.reyesemf.gm.article.infrastructure.CategoryValidator;
import com.reyesemf.gm.article.infrastructure.Sluggify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final Consumer<Category> categoryValidator = new CategoryValidator();
    private final Function<String, String> sluggify = new Sluggify();

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createOrUpdate(Category candidate) {
        categoryValidator.accept(candidate);

        Category category;
        if (nonNull(candidate.getSlug())) {
            category = categoryRepository.findBySlug(candidate.getSlug())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + candidate.getSlug()));
        } else {
            category = new Category();
        }

        category.setName(candidate.getName());
        category.setSlug(sluggify.apply(candidate.getName()));
        category.setDescription(candidate.getDescription());
        category.setImage(candidate.getImage());
        category.setUrl(candidate.getUrl());

        return categoryRepository.save(category);

    }

    @Override
    public void delete(Category candidate) {
        requireNonNull(candidate, "Category must be not null");
        requireNonNull(candidate.getId(), "Category.id must be not null");
        categoryRepository.findById(candidate.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found category_id: " + candidate.getId()));
        categoryRepository.deleteById(candidate.getId());
    }

}
