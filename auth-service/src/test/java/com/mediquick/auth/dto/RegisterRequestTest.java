package com.mediquick.auth.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
    }

    @Test
    void defaultConstructor_CreatesEmptyObject() {
        assertNull(registerRequest.getEmail());
        assertNull(registerRequest.getPassword());
        assertNull(registerRequest.getName());
    }

    @Test
    void setEmail_UpdatesEmail() {
        registerRequest.setEmail("test@gmail.com");
        assertEquals("test@gmail.com", registerRequest.getEmail());
    }

    @Test
    void setPassword_UpdatesPassword() {
        registerRequest.setPassword("password123");
        assertEquals("password123", registerRequest.getPassword());
    }

    @Test
    void setName_UpdatesName() {
        registerRequest.setName("Test User");
        assertEquals("Test User", registerRequest.getName());
    }

    @Test
    void setAllFields_ReturnsCorrectValues() {
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("Test User");

        assertEquals("test@gmail.com", registerRequest.getEmail());
        assertEquals("password123", registerRequest.getPassword());
        assertEquals("Test User", registerRequest.getName());
    }
}