package com.carrental.controller;


import com.carrental.models.User;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class UserRestControllerTest {
	private static final Logger logger = LogManager.getLogger(UserRestControllerTest.class);
    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    	        logger.info("Setting up UserRestControllerTest");
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        logger.info("MockMvc setup complete");
    }

    @Test
    void createUserTest() throws Exception {
    	logger.info("Running createUserTest");
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setPhone("123-456-7890");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content("{\"name\": \"John Doe\", \"email\": \"john@example.com\", \"password\": \"password123\", \"phone\": \"123-456-7890\", \"address\": \"123 Main St\", \"isAdmin\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.isAdmin").value(false));

        verify(userService, times(1)).registerUser(any(User.class));
        logger.info("User created successfully");
    }

    @Test
    void createUserTestBadRequest() throws Exception {
    	logger.info("Running createUserTestBadRequest");
        User user = new User();
        user.setName("John Doe");
        user.setEmail("");
        user.setPassword("password123");
        user.setPhone("123-456-7890");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);

        when(userService.registerUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid user data"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content("{\"name\": \"John Doe\", \"email\": \"\", \"password\": \"password123\", \"phone\": \"123-456-7890\", \"address\": \"123 Main St\", \"isAdmin\": false}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(any(User.class));
        logger.info("Bad request for user creation");
    }

    @Test
    void getUserTest() throws Exception {
    	logger.info("Running getUserTest");
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("123-456-7890");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.isAdmin").value(false));

        verify(userService, times(1)).getUserById(1L);
        logger.info("User retrieved successfully");
    }

    @Test
    void getUserTestNotFound() throws Exception {
    	logger.info("Running getUserTestNotFound");
        when(userService.getUserById(999L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999L);
        logger.info("User not found");
    }
}