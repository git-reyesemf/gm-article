package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.domain.service.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
@Sql(scripts = "classpath:/db/mysql/schema.sql")
@Sql(scripts = "classpath:/db/mysql/data.sql")
public class ArticleServiceImplTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void givenNonExistCategorySlugWhenGetAllByCategorySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> articleService.getAllByCategorySlug("unknown"));
    }

    @Test
    public void givenExistCategorySlugWhenGetAllByCategorySlugThenReturnAllCategoryArticles() {
        assertEquals(20, articleService.getAllByCategorySlug("automotores").size());
    }

    @Test
    public void givenNonExistArticleSlugWhenGetBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> articleService.getBySlug("unknown"));
    }

    @Test
    public void givenNonExistArticleSlugWhenGetBySlugThenDoesNotThrow() {
        assertDoesNotThrow(() -> articleService.getBySlug("mobil-1-advanced-5w30"));
    }

}