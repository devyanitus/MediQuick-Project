package com.mediquick.consultant.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private String secret;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        secret = "mytestsecretkeymytestsecretkey12";
        ReflectionTestUtils.setField(jwtService, "secret", secret);
    }

    @Test
    void shouldValidateTokenWhenTokenIsCorrect() {
        String token = generateToken("user@example.com");

        boolean result = jwtService.validateToken(token);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalid() {
        boolean result = jwtService.validateToken("invalid.token.value");

        assertFalse(result);
    }

    @Test
    void shouldExtractEmailFromToken() {
        String token = generateToken("user@example.com");

        String email = jwtService.extractEmail(token);

        assertEquals("user@example.com", email);
    }

    private String generateToken(String email) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}