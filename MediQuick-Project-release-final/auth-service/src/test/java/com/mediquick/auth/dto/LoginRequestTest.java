package com.mediquick.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    void shouldSetAndGetEmailAndPassword() {
        LoginRequest request = new LoginRequest();

        request.setEmail("test@example.com");
        request.setPassword("secret123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("secret123", request.getPassword());
    }
}