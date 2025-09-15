package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.ArticleRepository;
import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
import com.reyesemf.gm.article.domain.service.ArticleService;
import com.reyesemf.gm.article.domain.service.CategoryService;
import com.reyesemf.gm.article.infrastructure.ArticleValidator;
import com.reyesemf.gm.article.infrastructure.Sluggify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final Consumer<Article> articleValidator = new ArticleValidator();
    private final Function<String, String> sluggify = new Sluggify();

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleRepository articleRepository;


    @Override
    public List<Article> getAllByCategorySlug(String categorySlug) {
        Category category = categoryService.getBySlug(categorySlug);
        return articleRepository.findAllByCategoryId(category.getId());
    }

    @Override
    public Article createOrUpdate(Article candidate) {
        articleValidator.accept(candidate);

        Article article;
        if (nonNull(candidate.getSlug())) {
            article = articleRepository.findBySlug(candidate.getSlug())
                    .orElseThrow(() -> new EntityNotFoundException("Article not found with slug: " + candidate.getSlug()));
        } else {
            article = new Article();
        }

        Category category = categoryService.getBySlug(candidate.getCategorySlug());

        article.setName(candidate.getName());
        article.setSlug(sluggify.apply(candidate.getName()));
        article.setDescription(candidate.getDescription());
        article.setImage(candidate.getImage());
        article.setUrl(candidate.getUrl());
        article.setCategory(category);

        return articleRepository.save(article);
    }

    @Override
    @Transactional(readOnly = true)
    public Article getBySlug(String articleSlug) {
        requireNonNull(articleSlug, "Article Slug must be not null");
        Article article = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new EntityNotFoundException("Not found article with slug: " + articleSlug));
        
        // Forzar inicialización de relatedMedia dentro de la transacción
        article.getRelatedMedia().size();
        
        return article;
    }

    @Override
    public void deleteBySlug(String articleSlug) {
        requireNonNull(articleSlug, "Article Slug must be not null");
        Article candidate = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new EntityNotFoundException("Not found Article with slug: " + articleSlug));
        articleRepository.deleteById(candidate.getId());
    }

}
