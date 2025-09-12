package com.reyesemf.gm.article.datasource.repository;

import com.reyesemf.gm.article.domain.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.slug = :slug")
    Optional<Article> findBySlug(@Param("slug") String slug);
    
    @Query("SELECT a FROM Article a JOIN a.category c WHERE c.slug = :categorySlug")
    List<Article> findAllByCategorySlug(@Param("categorySlug") String categorySlug);

}
