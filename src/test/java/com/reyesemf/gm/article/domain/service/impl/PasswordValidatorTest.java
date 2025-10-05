package com.reyesemf.gm.article.domain.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordValidatorTest {
    
    private final BiConsumer<String, String> passwordValidator = new PasswordValidator();

    @Test
    public void givenValidSha256HashesWhenAcceptThenDoesNotThrow() {
        // Valid SHA-256 hashes (64 hexadecimal characters)
        String validHash1 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String validHash2 = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        String validHash3 = "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae";
        String validHash4 = "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9";
        
        // Same hash twice (passwords match)
        assertDoesNotThrow(() -> passwordValidator.accept(validHash1, validHash1));
        assertDoesNotThrow(() -> passwordValidator.accept(validHash2, validHash2));
        assertDoesNotThrow(() -> passwordValidator.accept(validHash3, validHash3));
        assertDoesNotThrow(() -> passwordValidator.accept(validHash4, validHash4));
        
        // Uppercase hexadecimal characters should also work
        String uppercaseHash = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";
        assertDoesNotThrow(() -> passwordValidator.accept(uppercaseHash, uppercaseHash));
        
        // Mixed case should work
        String mixedCaseHash = "E3b0C44298fc1C149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertDoesNotThrow(() -> passwordValidator.accept(mixedCaseHash, mixedCaseHash));
    }

    @Test
    public void givenInvalidSha256FormatWhenAcceptThenThrowsIllegalArgumentException() {
        String validHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        
        // Too short (63 characters)
        String tooShort = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b85";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(tooShort, validHash));
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(validHash, tooShort));
        
        // Too long (65 characters)
        String tooLong = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b8555";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(tooLong, validHash));
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(validHash, tooLong));
        
        // Invalid characters (contains 'g')
        String invalidChars = "g3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(invalidChars, validHash));
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(validHash, invalidChars));
        
        // Contains special characters
        String specialChars = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b85@";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(specialChars, validHash));
        
        // Contains spaces
        String withSpaces = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b8 5";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(withSpaces, validHash));
        
        // Empty string
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept("", validHash));
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(validHash, ""));
        
        // Plain text password
        String plainText = "mypassword123";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(plainText, validHash));
        
        // MD5 hash (32 characters)
        String md5Hash = "5d41402abc4b2a76b9719d911017c592";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(md5Hash, validHash));
        
        // SHA-1 hash (40 characters)
        String sha1Hash = "aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d";
        assertThrows(IllegalArgumentException.class, () -> passwordValidator.accept(sha1Hash, validHash));
    }

    @Test
    public void givenDifferentValidHashesWhenAcceptThenThrowsBadCredentialsException() {
        String hash1 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String hash2 = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        
        // Both are valid SHA-256 hashes but different
        assertThrows(BadCredentialsException.class, () -> passwordValidator.accept(hash1, hash2));
        assertThrows(BadCredentialsException.class, () -> passwordValidator.accept(hash2, hash1));
    }

    @Test
    public void givenNullPasswordsWhenAcceptThenThrowsNullPointerException() {
        String validHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        
        assertThrows(NullPointerException.class, () -> passwordValidator.accept(null, validHash));
        assertThrows(NullPointerException.class, () -> passwordValidator.accept(validHash, null));
        assertThrows(NullPointerException.class, () -> passwordValidator.accept(null, null));
    }

    @Test
    public void givenCaseSensitiveHashesWhenAcceptThenValidatesCorrectly() {
        String lowerCase = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String upperCase = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";
        
        // Same hash in different cases should be treated as different
        assertThrows(BadCredentialsException.class, () -> passwordValidator.accept(lowerCase, upperCase));
        assertThrows(BadCredentialsException.class, () -> passwordValidator.accept(upperCase, lowerCase));
        
        // But same case should work
        assertDoesNotThrow(() -> passwordValidator.accept(lowerCase, lowerCase));
        assertDoesNotThrow(() -> passwordValidator.accept(upperCase, upperCase));
    }

    @Test
    public void givenEdgeCaseHashesWhenAcceptThenValidatesCorrectly() {
        // All zeros (valid SHA-256)
        String allZeros = "0000000000000000000000000000000000000000000000000000000000000000";
        assertDoesNotThrow(() -> passwordValidator.accept(allZeros, allZeros));
        
        // All F's (valid SHA-256)
        String allFs = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        assertDoesNotThrow(() -> passwordValidator.accept(allFs, allFs));
        
        // Mix of all valid hex characters
        String mixedHex = "0123456789abcdefABCDEF0123456789abcdefABCDEF0123456789abcdefABCD";
        assertDoesNotThrow(() -> passwordValidator.accept(mixedHex, mixedHex));
    }
}
