package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.ArticleRepository;
import com.reyesemf.gm.article.datasource.repository.CategoryRepository;
import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
import com.reyesemf.gm.article.domain.service.ArticleService;
import com.reyesemf.gm.article.infrastructure.ArticleValidator;
import com.reyesemf.gm.article.infrastructure.Sluggify;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final Consumer<Article> articleValidator = new ArticleValidator();
    private final Function<String, String> sluggify = new Sluggify();

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        Category category = categoryRepository.findBySlug(candidate.getCategorySlug())
                        .orElseThrow(() -> new EntityNotFoundException("Not found category with slug: " + candidate.getCategorySlug()));

        article.setName(candidate.getName());
        article.setSlug(sluggify.apply(candidate.getName()));
        article.setDescription(candidate.getDescription());
        article.setImage(candidate.getImage());
        article.setUrl(candidate.getUrl());
        article.setCategory(category);

        return articleRepository.save(article);
    }

    @Override
    public void delete(Article candidate) {
        requireNonNull(candidate, "Article must be not null");
        requireNonNull(candidate.getId(), "Article.id must be not null");
        articleRepository.findById(candidate.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found article_id: " + candidate.getId()));
        articleRepository.deleteById(candidate.getId());
    }

}
