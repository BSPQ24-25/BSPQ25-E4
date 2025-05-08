package com.carrental.service;

import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class UserServiceTest {
	private static final Logger logger = LogManager.getLogger(BookingServiceTest.class);
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
		logger.info("Setting up UserServiceTest");
		
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setAddress("123 Street");
        user.setPhone("123456789");
        user.setIsAdmin(false);
        logger.info("Mocks initialized");
    }

    @Test
    void registerUser_success() {
		logger.info("Testing registerUser method");

		
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
        logger.info("User registered successfully: {}", result.getEmail());
    }

    @Test
    void registerUser_emailInUse_throwsException() {
    	logger.info("Testing registerUser method with email in use");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("The email is in use", exception.getMessage());
        logger.info("Email already in use: {}", user.getEmail());
    }

    @Test
    void findByEmail_success() {
    	logger.info("Testing findByEmail method");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User found = userService.findByEmail("john@example.com");

        assertEquals("john@example.com", found.getEmail());
        logger.info("User found: {}", found.getEmail());
    }

    @Test
    void findByEmail_notFound_throwsException() {
    	logger.info("Testing findByEmail method for a non-existing user");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findByEmail("john@example.com");
        });

        assertEquals("User not found", exception.getMessage());
        logger.info("Testing findByEmail method for a non-existing user done");
    }

    @Test
    void getAllUsers_returnsList() {
		logger.info("Testing getAllUsers method");
		
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        logger.info("Users retrieved successfully: {}", result);
    }

    @Test
    void getUserById_found() {
		logger.info("Testing getUserById method");
		
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        logger.info("User found: {}", result.getId());
    }

    @Test
    void getUserById_notFound_throwsException() {
    	logger.info("Testing getUserById method for a non-existing user");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found", exception.getMessage());
        logger.info("User not found for ID: 1");
    }

    @Test
    void updateUser_success() {
    	logger.info("Testing updateUser method");
        User updated = new User();
        updated.setName("Jane Doe");
        updated.setEmail("jane@example.com");
        updated.setAddress("New Address");
        updated.setPhone("987654321");
        updated.setIsAdmin(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(1L, updated);

        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        verify(userRepository).save(user);
        logger.info("User updated successfully: {}", user.getEmail());
    }

    @Test
    void deleteUser_success() {
		logger.info("Testing deleteUser method");
		
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        logger.info("User deleted successfully with ID: {}", 1L);
    }
}