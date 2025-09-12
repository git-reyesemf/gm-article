package com.reyesemf.gm.article.datasource.repository;

import com.reyesemf.gm.article.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.slug = :slug")
    Optional<Category> findBySlug(@Param("slug") String slug);
}
