package com.carrental.repository;

import com.carrental.models.User;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@DataJpaTest
class UserRepositoryTest {
	private static final Logger logger = LogManager.getLogger(UserRepositoryTest.class);
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_shouldReturnUser() {
    	logger.info("Testing findByEmail method");
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
        logger.info("User found: {}", found.get().getEmail());
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
    	logger.info("Testing existsByEmail method");
        User user = new User();
        user.setEmail("unique@example.com");
        user.setPassword("123456");
        user.setName("Unique User");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("unique@example.com"));
        logger.info("User exists: {}", user.getEmail());
    }

    @Test
    void existsByEmail_shouldReturnFalse() {
    	logger.info("Testing existsByEmail method for non-existing email");
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
        
    }

    @Test
    void findByEmail_shouldReturnEmptyIfNotFound() {
    	logger.info("Testing findByEmail method for non-existing email");
        Optional<User> result = userRepository.findByEmail("unknown@example.com");
        assertTrue(result.isEmpty());
        
    }
}