package com.reyesemf.gm.article.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlValidatorTest {
    private final Consumer<String> urlValidator = new UrlValidator();

    @Test
    public void givenValidUrlWhenAcceptThenDoesNotThrow() {
        assertDoesNotThrow(() -> urlValidator.accept("http://www.example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://www.example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("http://subdomain.example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://subdomain.example.com"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com/path"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/path"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com/path/to/resource"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/path/to/resource"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com:8080"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com:8443"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com:80"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com:443"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example.com:8080/path"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com:8443/secure"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/search?q=test"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/page?param1=value1&param2=value2"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/page#section"));
        assertDoesNotThrow(() -> urlValidator.accept("https://example.com/page?q=test#section"));
        assertDoesNotThrow(() -> urlValidator.accept("http://192.168.1.1"));
        assertDoesNotThrow(() -> urlValidator.accept("https://127.0.0.1:8080"));
        assertDoesNotThrow(() -> urlValidator.accept("http://10.0.0.1"));
        assertDoesNotThrow(() -> urlValidator.accept("https://172.16.0.1"));
        assertDoesNotThrow(() -> urlValidator.accept("http://api.service.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://cdn.example.org"));
        assertDoesNotThrow(() -> urlValidator.accept("http://www.site-with-dashes.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://site-with-dashes.org"));
        assertDoesNotThrow(() -> urlValidator.accept("http://example123.com"));
        assertDoesNotThrow(() -> urlValidator.accept("https://123example.com"));
    }
    
    @Test
    public void givenInvalidUrlWhenAcceptThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("ftp://example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("file://path/to/file"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("mailto:user@example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("javascript:alert(1)"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("data:text/html,<script>"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("not-a-url"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("www.example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("://example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http:/"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https:/"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http:example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https:example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example .com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://example..com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://.example.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://example."));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept(""));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("   "));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("simple text"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("product description"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://[invalid-ipv6]"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://user:pass@"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example.com:99999"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://example.com:-1"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example.com:abc"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://exam ple.com"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example.com/path with spaces"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://example.com/path|pipe"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://example.com/path<tag>"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://example.com/path\"quote"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://256.256.256.256"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://999.999.999.999"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("http://192.168.1."));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://.192.168.1.1"));
        assertThrows(IllegalArgumentException.class, () -> urlValidator.accept("https://site_with_underscores.com"));
    }
    
    @Test
    public void givenNullUrlWhenAcceptThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> urlValidator.accept(null));
    }

}