package com.carrental.service;

import com.carrental.models.User;
import com.carrental.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class CustomUserDetailsServiceTest {
	private static final Logger logger = LogManager.getLogger(CustomUserDetailsServiceTest.class);
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up CustomUserDetailsServiceTest");
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@example.com");
        user.setPassword("securepass123");
        user.setIsAdmin(false);
        logger.info("Mocks initialized");
    }

    @Test
    void loadUserByUsername_userFound_asUser() {
    	logger.info("Testing loadUserByUsername method for a regular user");
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("juan@example.com");

        assertNotNull(userDetails);
        assertEquals("juan@example.com", userDetails.getUsername());
        assertEquals("securepass123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("juan@example.com");
        logger.info("User found: {}", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_userFound_asAdmin() {
    	logger.info("Testing loadUserByUsername method for an admin user");
        user.setIsAdmin(true);
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("juan@example.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        logger.info("Admin user found: {}", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
    	logger.info("Testing loadUserByUsername method for a non-existing user");
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("notfound@example.com");
        });

        verify(userRepository).findByEmail("notfound@example.com");
        logger.info("User not found, exception thrown");
    }
}