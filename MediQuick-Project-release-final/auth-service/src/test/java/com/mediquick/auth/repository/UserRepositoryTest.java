package com.mediquick.auth.repository;

import com.mediquick.auth.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find user by email")
    void findByEmail_shouldReturnUser_whenEmailExists() {
        User user = new User("john@example.com", "password123");
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("notfound@example.com");

        assertFalse(result.isPresent());
    }
}