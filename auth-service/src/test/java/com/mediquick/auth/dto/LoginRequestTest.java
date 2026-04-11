package com.mediquick.auth.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void defaultConstructor_CreatesEmptyObject() {
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }

    @Test
    void setEmail_UpdatesEmail() {
        loginRequest.setEmail("test@gmail.com");
        assertEquals("test@gmail.com", loginRequest.getEmail());
    }

    @Test
    void setPassword_UpdatesPassword() {
        loginRequest.setPassword("password123");
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void setAllFields_ReturnsCorrectValues() {
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("password123");

        assertEquals("test@gmail.com", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
    }
}