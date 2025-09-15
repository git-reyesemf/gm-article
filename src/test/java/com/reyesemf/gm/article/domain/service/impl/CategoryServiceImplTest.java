package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.domain.service.CategoryService;
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
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;


    @Test
    public void givenExistCategoriesWhenGetAllThenReturnAllCategories() {
        assertEquals(4, categoryService.getAll().size());
    }

    @Test
    public void givenNonExistCategorySlugWhenGetBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> categoryService.getBySlug("unknown"));
    }

    @Test
    public void givenExistCategorySlugWhenGetBySlugThenDoesNotThrow() {
        assertDoesNotThrow(() -> categoryService.getBySlug("automotores"));
    }

}