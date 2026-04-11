package com.mediquick.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "mediquickSecretKeyForJwtGeneration123456");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    // ===== generateToken =====

    @Test
    void generateToken_ReturnsNonNullToken() {
        String token = jwtService.generateToken("test@gmail.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_HasThreeParts() {
        String token = jwtService.generateToken("test@gmail.com");
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    void generateToken_DifferentEmailsProduceDifferentTokens() {
        String token1 = jwtService.generateToken("user1@gmail.com");
        String token2 = jwtService.generateToken("user2@gmail.com");
        assertNotEquals(token1, token2);
    }

    // ===== extractEmail =====

    @Test
    void extractEmail_ReturnsCorrectEmail() {
        String token = jwtService.generateToken("test@gmail.com");
        String email = jwtService.extractEmail(token);
        assertEquals("test@gmail.com", email);
    }

    @Test
    void extractEmail_InvalidToken_ThrowsException() {
        assertThrows(Exception.class, () ->
                jwtService.extractEmail("invalid.token.here")
        );
    }

    // ===== isTokenValid =====

    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        String token = jwtService.generateToken("test@gmail.com");
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_InvalidToken_ReturnsFalse() {
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }

    @Test
    void isTokenValid_TamperedToken_ReturnsFalse() {
        String token = jwtService.generateToken("test@gmail.com");
        assertFalse(jwtService.isTokenValid(token + "tampered"));
    }

    @Test
    void isTokenValid_EmptyToken_ReturnsFalse() {
        assertFalse(jwtService.isTokenValid(""));
    }

    // ===== expiration =====

    @Test
    void generateToken_ExpiredToken_IsInvalid() {
        // Set expiration to 1ms so it expires immediately
        ReflectionTestUtils.setField(jwtService, "expiration", 1L);
        String token = jwtService.generateToken("test@gmail.com");

        try { Thread.sleep(10); } catch (InterruptedException e) {}

        assertFalse(jwtService.isTokenValid(token));
    }
}