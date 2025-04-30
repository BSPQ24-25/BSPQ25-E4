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

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@example.com");
        user.setPassword("securepass123");
        user.setIsAdmin(false);
    }

    @Test
    void loadUserByUsername_userFound_asUser() {
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("juan@example.com");

        assertNotNull(userDetails);
        assertEquals("juan@example.com", userDetails.getUsername());
        assertEquals("securepass123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("juan@example.com");
    }

    @Test
    void loadUserByUsername_userFound_asAdmin() {
        user.setIsAdmin(true);
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("juan@example.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("notfound@example.com");
        });

        verify(userRepository).findByEmail("notfound@example.com");
    }
}