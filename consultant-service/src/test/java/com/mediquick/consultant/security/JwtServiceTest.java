package com.mediquick.consultant.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private String secret = "mediquickSecretKeyForJwtGeneration123456";
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Inject secret into service
        ReflectionTestUtils.setField(jwtService, "secret", secret);

        // ✅ Generate token dynamically (NO dependency on auth-service)
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        validToken = Jwts.builder()
                .setSubject("test@gmail.com")
                .signWith(key)
                .compact();
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {
        assertTrue(jwtService.validateToken(validToken));
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsInvalid() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtService.validateToken(invalidToken));
    }

    @Test
    void extractEmail_ShouldReturnEmail_WhenTokenIsValid() {
        String email = jwtService.extractEmail(validToken);
        assertEquals("test@gmail.com", email);
    }

    @Test
    void extractEmail_ShouldThrowException_WhenTokenIsInvalid() {
        String invalidToken = "invalid.token.value";

        assertThrows(Exception.class, () -> {
            jwtService.extractEmail(invalidToken);
        });
    }
}