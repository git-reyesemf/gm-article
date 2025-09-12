package com.reyesemf.gm.article.domain.service.impl;

import com.reyesemf.gm.article.datasource.repository.ArticleRepository;
import com.reyesemf.gm.article.datasource.repository.CategoryRepository;
import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
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

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void givenValidSlugWhenGetBySlugThenReturnsArticle() {
        Category category = createAndSaveCategory("Books", "books");
        Article article = createValidArticle("Book Guide", "Complete book guide", "https://example.com/guide.jpg", "https://example.com/guide", "books");
        Article savedArticle = articleService.createOrUpdate(article);
        
        Article result = articleService.getBySlug(savedArticle.getSlug());
        
        assertNotNull(result);
        assertEquals(savedArticle.getId(), result.getId());
        assertEquals("Book Guide", result.getName());
        assertEquals("book-guide", result.getSlug());
        assertEquals("Complete book guide", result.getDescription());
    }

    @Test
    public void givenNonExistentSlugWhenGetBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> articleService.getBySlug("non-existent-slug"));
    }

    @Test
    public void givenNullSlugWhenGetBySlugThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> articleService.getBySlug(null));
    }

    @Test
    public void givenValidArticleWhenCreateOrUpdateThenGeneratesCorrectSlug() {
        Category category = createAndSaveCategory("Books", "books");
        Article article = createValidArticle("Book Review", "Amazing book analysis", "https://example.com/books.jpg", "https://example.com/books", "books");
        
        Article result = articleService.createOrUpdate(article);
        
        assertNotNull(result);
        assertEquals("Book Review", result.getName());
        assertEquals("book-review", result.getSlug());
    }

    @Test
    public void givenArticleWithAccentsWhenCreateOrUpdateThenGeneratesSlugWithoutAccents() {
        Category category = createAndSaveCategory("Fashion", "fashion");
        Article article = createValidArticle("Artículo", "Descripción", "https://example.com/fashion.jpg", "https://example.com/fashion", "fashion");
        
        Article result = articleService.createOrUpdate(article);
        
        assertNotNull(result);
        assertEquals("Artículo", result.getName());
        assertEquals("articulo", result.getSlug());
    }

    @Test
    public void givenExistingArticleWhenCreateOrUpdateThenUpdatesExistingArticle() {
        Category category = createAndSaveCategory("Sports", "sports");
        
        Article initialArticle = createValidArticle("Old Sports", "Old description", "https://old.com/image.jpg", "https://old.com/sports", "sports");
        Article savedArticle = articleService.createOrUpdate(initialArticle);
        
        Article updateCandidate = createValidArticle("Updated Sports", "Updated description", "https://updated.com/image.jpg", "https://updated.com/sports", "sports");
        updateCandidate.setSlug(savedArticle.getSlug());
        
        Article result = articleService.createOrUpdate(updateCandidate);
        
        assertNotNull(result);
        assertEquals("Updated Sports", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(savedArticle.getId(), result.getId());
    }

    @Test
    public void givenNonExistentSlugWhenCreateOrUpdateThenThrowsEntityNotFoundException() {
        Category category = createAndSaveCategory("Music", "music");
        Article article = createValidArticle("Music News", "Music updates", "https://example.com/music.jpg", "https://example.com/music", "music");
        article.setSlug("non-existent-slug");
        
        assertThrows(EntityNotFoundException.class, () -> articleService.createOrUpdate(article));
    }

    @Test
    public void givenNonExistentCategorySlugWhenCreateOrUpdateThenThrowsEntityNotFoundException() {
        Article article = createValidArticle("Tech News", "Technology updates", "https://example.com/tech.jpg", "https://example.com/tech", "non-existent-category");
        
        assertThrows(EntityNotFoundException.class, () -> articleService.createOrUpdate(article));
    }

    @Test
    public void givenArticleWithSpecialCharactersWhenCreateOrUpdateThenGeneratesCleanSlug() {
        Category category = createAndSaveCategory("Games", "games");
        Article article = createValidArticle("Game Review: The Best RPG!", "Amazing game analysis", "https://example.com/games.jpg", "https://example.com/games", "games");
        
        Article result = articleService.createOrUpdate(article);
        
        assertNotNull(result);
        assertEquals("Game Review: The Best RPG!", result.getName());
        assertEquals("game-review-the-best-rpg", result.getSlug());
    }

    @Test
    public void givenMultipleArticlesWhenCreateOrUpdateThenEachHasUniqueSlug() {
        Category category = createAndSaveCategory("Food", "food");
        
        Article article1 = createValidArticle("Recipe Guide", "Cooking tips", "https://example.com/recipe1.jpg", "https://example.com/recipe1", "food");
        Article article2 = createValidArticle("Recipe Tutorial", "Baking guide", "https://example.com/recipe2.jpg", "https://example.com/recipe2", "food");
        
        Article result1 = articleService.createOrUpdate(article1);
        Article result2 = articleService.createOrUpdate(article2);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getSlug(), result2.getSlug());
        assertEquals("recipe-guide", result1.getSlug());
        assertEquals("recipe-tutorial", result2.getSlug());
    }

    @Test
    public void givenInvalidArticleWithSSRFWhenCreateOrUpdateThenThrowsException() {
        Category category = createAndSaveCategory("Security", "security");
        Article article = createInvalidArticle("localhost", "Local server article", "https://example.com/image.jpg", "https://example.com/local", "security");
        
        assertThrows(IllegalArgumentException.class, () -> articleService.createOrUpdate(article));
    }

    @Test
    public void givenInvalidArticleWithBadUrlWhenCreateOrUpdateThenThrowsException() {
        Category category = createAndSaveCategory("Electronics", "electronics");
        Article article = createInvalidArticle("Electronics", "Electronic products", "not-a-url", "https://example.com/electronics", "electronics");
        
        assertThrows(IllegalArgumentException.class, () -> articleService.createOrUpdate(article));
    }

    @Test
    public void givenInvalidArticleWithXSSWhenCreateOrUpdateThenThrowsException() {
        Category category = createAndSaveCategory("Fashion", "fashion");
        Article article = createInvalidArticle("Fashion", "<script>alert('xss')</script>", "https://example.com/fashion.jpg", "https://example.com/fashion", "fashion");
        
        assertThrows(IllegalArgumentException.class, () -> articleService.createOrUpdate(article));
    }

    @Test
    public void givenValidArticleWhenDeleteBySlugThenRemovesFromRepository() {
        Category category = createAndSaveCategory("Sports", "sports");
        Article article = createValidArticle("Sports", "Sports equipment", "https://example.com/sports.jpg", "https://example.com/sports", "sports");
        Article savedArticle = articleService.createOrUpdate(article);
        Long articleId = savedArticle.getId();
        
        assertTrue(articleRepository.existsById(articleId));
        
        articleService.deleteBySlug(savedArticle.getSlug());
        
        assertFalse(articleRepository.existsById(articleId));
    }

    @Test
    public void givenNullSlugWhenDeleteBySlugThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> articleService.deleteBySlug(null));
    }

    @Test
    public void givenEmptySlugWhenDeleteBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> articleService.deleteBySlug(""));
    }

    @Test
    public void givenNonExistentSlugWhenDeleteBySlugThenThrowsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> articleService.deleteBySlug("non-existent-slug"));
    }

    private Article createValidArticle(String name, String description, String image, String url, String categorySlug) {
        Article article = new Article();
        article.setName(name);
        article.setDescription(description);
        article.setImage(image);
        article.setUrl(url);
        article.setCategorySlug(categorySlug);
        Category dummyCategory = new Category();
        dummyCategory.setName("dummy");
        article.setCategory(dummyCategory);
        return article;
    }

    private Article createInvalidArticle(String name, String description, String image, String url, String categorySlug) {
        Article article = new Article();
        article.setName(name);
        article.setDescription(description);
        article.setImage(image);
        article.setUrl(url);
        article.setCategorySlug(categorySlug);
        Category dummyCategory = new Category();
        dummyCategory.setName("dummy");
        article.setCategory(dummyCategory);
        return article;
    }

    private Category createAndSaveCategory(String name, String slug) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(name + " articles");
        category.setImage("https://example.com/" + slug + ".jpg");
        category.setUrl("https://example.com/" + slug);
        return categoryRepository.save(category);
    }
}