package com.carrental.repository;

import com.carrental.models.User;
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
    void findByEmail_shouldReturnUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("Test User");
        user.setIsAdmin(false);
        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        User user = new User();
        user.setEmail("unique@example.com");
        user.setPassword("123456");
        user.setName("Unique User");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("unique@example.com"));
    }

    @Test
    void existsByEmail_shouldReturnFalse() {
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void findByEmail_shouldReturnEmptyIfNotFound() {
        Optional<User> result = userRepository.findByEmail("unknown@example.com");
        assertTrue(result.isEmpty());
    }
}