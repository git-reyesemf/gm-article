package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.CategoryRepository;
import com.reyesemf.gm.article.domain.model.Category;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenDatabaseInitializedWhenSaveCategoryThenSuccess() {
        Category category = new Category();
        category.setName("Books");
        category.setSlug("books");
        category.setDescription("Literature collection");
        category.setImage("https://example.com/books.jpg");
        category.setUrl("https://example.com/books");
        
        Category saved = categoryRepository.save(category);
        
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Books", saved.getName());
        assertEquals("books", saved.getSlug());
        
        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Books", found.getName());
    }

    @Test
    public void givenValidSlugWhenGetBySlugThenReturnsCategory() {
        Category category = createValidCategory("Technology", "Tech products and services", "https://example.com/tech.jpg", "https://example.com/tech");
        Category savedCategory = categoryService.createOrUpdate(category);
        
        Category result = categoryService.getBySlug(savedCategory.getSlug());
        
        assertNotNull(result);
        assertEquals(savedCategory.getId(), result.getId());
        assertEquals("Technology", result.getName());
        assertEquals("technology", result.getSlug());
        assertEquals("Tech products and services", result.getDescription());
    }

    @Test
    public void givenNonExistentSlugWhenGetBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> categoryService.getBySlug("non-existent-slug"));
    }

    @Test
    public void givenNullSlugWhenGetBySlugThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> categoryService.getBySlug(null));
    }

    @Test
    public void givenValidCategoryWhenCreateOrUpdateThenGeneratesCorrectSlug() {
        Category category = createValidCategory("Home & Garden", "Home and garden products", "https://example.com/home.jpg", "https://example.com/home");
        
        Category result = categoryService.createOrUpdate(category);
        
        assertNotNull(result);
        assertEquals("Home & Garden", result.getName());
        assertEquals("home-garden", result.getSlug());
    }

    @Test
    public void givenCategoryWithAccentsWhenCreateOrUpdateThenGeneratesSlugWithoutAccents() {
        Category category = createValidCategory("Electrónicos", "Productos electrónicos", "https://example.com/electronics.jpg", "https://example.com/electronics");
        
        Category result = categoryService.createOrUpdate(category);
        
        assertNotNull(result);
        assertEquals("Electrónicos", result.getName());
        assertEquals("electronicos", result.getSlug());
    }

    @Test
    public void givenExistingCategoryWhenCreateOrUpdateThenUpdatesExistingCategory() {
        Category savedCategory = createValidCategory("Old Books", "Old description", "https://old.com/image.jpg", "https://old.com/books");
        savedCategory.setSlug("old-books");
        savedCategory = categoryRepository.save(savedCategory);
        
        Category updateCandidate = createValidCategory("Updated Books", "Updated description", "https://updated.com/image.jpg", "https://updated.com/books");
        updateCandidate.setSlug("old-books");
        
        Category result = categoryService.createOrUpdate(updateCandidate);
        
        assertNotNull(result);
        assertEquals("Updated Books", result.getName());
        assertEquals("updated-books", result.getSlug());
        assertEquals("Updated description", result.getDescription());
        assertEquals("https://updated.com/image.jpg", result.getImage());
        assertEquals("https://updated.com/books", result.getUrl());
    }

    @Test
    public void givenNonExistentSlugWhenCreateOrUpdateThenThrowsEntityNotFoundException() {
        Category category = createValidCategory("Books", "Books and literature", "https://example.com/books.jpg", "https://example.com/books");
        category.setSlug("non-existent-slug");
        
        assertThrows(EntityNotFoundException.class, () -> categoryService.createOrUpdate(category));
    }

    @Test
    public void givenInvalidCategoryWithSSRFWhenCreateOrUpdateThenThrowsException() {
        Category category = createInvalidCategory("localhost", "Local server category", "https://example.com/image.jpg", "https://example.com/local");
        
        assertThrows(IllegalArgumentException.class, () -> categoryService.createOrUpdate(category));
    }

    @Test
    public void givenInvalidCategoryWithBadUrlWhenCreateOrUpdateThenThrowsException() {
        Category category = createInvalidCategory("Electronics", "Electronic products", "not-a-url", "https://example.com/electronics");
        
        assertThrows(IllegalArgumentException.class, () -> categoryService.createOrUpdate(category));
    }

    @Test
    public void givenInvalidCategoryWithXSSWhenCreateOrUpdateThenThrowsException() {
        Category category = createInvalidCategory("Fashion", "<script>alert('xss')</script>", "https://example.com/fashion.jpg", "https://example.com/fashion");
        
        assertThrows(IllegalArgumentException.class, () -> categoryService.createOrUpdate(category));
    }

    @Test
    public void givenValidSlugWhenDeleteBySlugThenRemovesFromRepository() {
        Category category = createValidCategory("Sports", "Sports equipment", "https://example.com/sports.jpg", "https://example.com/sports");
        Category savedCategory = categoryService.createOrUpdate(category);
        Long categoryId = savedCategory.getId();
        
        assertTrue(categoryRepository.existsById(categoryId));
        
        categoryService.deleteBySlug(savedCategory.getSlug());
        
        assertFalse(categoryRepository.existsById(categoryId));
    }

    @Test
    public void givenNonExistentSlugWhenDeleteBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteBySlug("non-existent-slug"));
    }

    @Test
    public void givenNullSlugWhenDeleteBySlugThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> categoryService.deleteBySlug(null));
    }

    @Test
    public void givenEmptySlugWhenDeleteBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteBySlug(""));
    }

    @Test
    public void givenMultipleCategoriesWhenCreateOrUpdateThenEachHasUniqueSlug() {
        Category category1 = createValidCategory("Technology", "Tech products", "https://example.com/tech1.jpg", "https://example.com/tech1");
        Category category2 = createValidCategory("Tech", "Technology items", "https://example.com/tech2.jpg", "https://example.com/tech2");
        
        Category result1 = categoryService.createOrUpdate(category1);
        Category result2 = categoryService.createOrUpdate(category2);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals("technology", result1.getSlug());
        assertEquals("tech", result2.getSlug());
        assertNotEquals(result1.getSlug(), result2.getSlug());
    }

    @Test
    public void givenCategoryWithSpecialCharactersWhenCreateOrUpdateThenGeneratesCleanSlug() {
        Category category = createValidCategory("Art & Crafts!", "Arts and crafts supplies", "https://example.com/art.jpg", "https://example.com/art");
        
        Category result = categoryService.createOrUpdate(category);
        
        assertNotNull(result);
        assertEquals("Art & Crafts!", result.getName());
        assertEquals("art-crafts", result.getSlug());
    }

    private Category createValidCategory(String name, String description, String image, String url) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImage(image);
        category.setUrl(url);
        return category;
    }

    private Category createInvalidCategory(String name, String description, String image, String url) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImage(image);
        category.setUrl(url);
        return category;
    }
}