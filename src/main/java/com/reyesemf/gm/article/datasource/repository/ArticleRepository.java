package com.reyesemf.gm.article.datasource.repository;

import com.reyesemf.gm.article.domain.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    List<Article> findAllByCategoryId(Long categoryId);
}
