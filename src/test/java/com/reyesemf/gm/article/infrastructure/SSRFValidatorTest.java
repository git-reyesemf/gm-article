package com.reyesemf.gm.article.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SSRFValidatorTest {
    private final Consumer<String> ssrfValidator = new SSRFValidator();

    @Test
    public void givenValidValueWhenAcceptThenDoesNotThrow() {
        assertDoesNotThrow(() -> ssrfValidator.accept("simple text"));
        assertDoesNotThrow(() -> ssrfValidator.accept("product description"));
        assertDoesNotThrow(() -> ssrfValidator.accept("category name"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Electronics"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Home & Garden"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Sports Equipment"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Books and Media"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Clothing & Accessories"));
        assertDoesNotThrow(() -> ssrfValidator.accept(""));
        assertDoesNotThrow(() -> ssrfValidator.accept("   "));
        assertDoesNotThrow(() -> ssrfValidator.accept("Product with numbers 123"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Special chars !@#$%"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Accented text áéíóú"));
        assertDoesNotThrow(() -> ssrfValidator.accept("Mixed CASE text"));
        assertDoesNotThrow(() -> ssrfValidator.accept("external-host.com"));
        assertDoesNotThrow(() -> ssrfValidator.accept("my-docs.com"));
    }
    
    @Test
    public void givenInvalidValueWhenAcceptThenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://www.google.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://public-api.example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://api.github.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://localhost"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://localhost"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://localhost:8080"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://LOCALHOST"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://LocalHost"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://127.0.0.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://127.0.0.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://127.0.0.1:8080"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://127.0.0.1:3000"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://0.0.0.0"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://0.0.0.0:8080"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://[::1]"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://[::1]:8080"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://internal.company.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://intranet.example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://INTERNAL.server.local"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://INTRANET.local"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://169.254.1.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://169.254.255.255"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://10.0.0.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://10.255.255.255"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://10.1.1.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://192.168.1.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://192.168.0.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://192.168.255.255"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://172.16.0.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("https://172.31.255.255"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("http://172.20.1.1"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("file:///etc/passwd"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("ftp://internal.server.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("gopher://example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("dict://localhost:2628"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("smb://internal.server/share"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("nfs://internal.server/mount"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("Check this localhost server"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("Connect to 127.0.0.1 for testing"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("Internal server documentation"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("Visit https://example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("Go to http://site.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("ldap://server.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("ssh://user@server"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("telnet://host"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("0x7f000001"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("2130706433"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("admin.server.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("test.example.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("staging environment"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("dev server"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("../../../etc/passwd"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("..\\..\\windows\\system32"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("%2e%2e%2f%2e%2e%2f"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("..%2f..%2f"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("..%5c..%5c"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("path%2ftraversal"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("file%5cpath"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("null%00byte"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("line%0abreak"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("carriage%0dreturn"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("&#x2e;&#x2e;/"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("&period;&period;/"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("%252e%252e%252f"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("\\\\server\\share"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("///triple/slash"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("javascript:alert(1)"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("data:text/html,<script>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("vbscript:msgbox(1)"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("about:blank"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("chrome://settings"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("jar:file:/path"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("view-source:http://"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("resource://internal"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("moz-icon://file"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("${jndi:ldap://}"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<%eval request%>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<?php system($_GET['cmd'])?>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<script>alert('xss')</script>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<iframe src='evil.com'>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<object data='malware.swf'>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("<embed src='plugin.dll'>"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("localhost-external.com"));
        assertThrows(IllegalArgumentException.class, () -> ssrfValidator.accept("my-internal-docs.com"));
    }
    
    @Test
    public void givenNullValueWhenAcceptThenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ssrfValidator.accept(null));
    }
}