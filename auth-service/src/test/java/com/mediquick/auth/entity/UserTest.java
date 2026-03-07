package com.mediquick.auth.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {

    @Test
    void shouldCreateUserWithConstructor() {
        User user = new User("john@example.com", "password123");

        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNull(user.getId());
    }

    @Test
    void shouldSetAndGetFields() {
        User user = new User();

        user.setEmail("test@example.com");
        user.setPassword("secret");

        assertEquals("test@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
    }
}