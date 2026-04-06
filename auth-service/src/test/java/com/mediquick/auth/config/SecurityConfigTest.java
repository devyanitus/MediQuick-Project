package com.mediquick.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    // ✅ Pass null - we only need to test passwordEncoder(), not the filter
    private final SecurityConfig securityConfig = new SecurityConfig(null);

    @Test
    void passwordEncoder_NotNull() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void passwordEncoder_EncodesPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("password123");

        assertNotNull(encoded);
        assertNotEquals("password123", encoded);
    }

    @Test
    void passwordEncoder_MatchesCorrectPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("password123");

        assertTrue(encoder.matches("password123", encoded));
    }

    @Test
    void passwordEncoder_DoesNotMatchWrongPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded = encoder.encode("password123");

        assertFalse(encoder.matches("wrongpassword", encoded));
    }

    @Test
    void passwordEncoder_SamePasswordEncodesDifferently() {
        // BCrypt salts each hash so same password → different encoded strings
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String encoded1 = encoder.encode("password123");
        String encoded2 = encoder.encode("password123");

        assertNotEquals(encoded1, encoded2);
        assertTrue(encoder.matches("password123", encoded1));
        assertTrue(encoder.matches("password123", encoded2));
    }
}