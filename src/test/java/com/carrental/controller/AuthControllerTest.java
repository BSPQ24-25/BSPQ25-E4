package com.carrental.controller;

import com.carrental.models.User;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class AuthControllerTest {
	private static final Logger logger = LogManager.getLogger(AuthControllerTest.class);
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up AuthControllerTest");
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(viewResolver)
                .build();
        logger.info("MockMvc setup complete");
    }

    @Test
    void testShowRegistrationForm() throws Exception {
    	logger.info("Running testShowRegistrationForm");
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
        logger.info("testShowRegistrationForm completed successfully");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
    	logger.info("Running testRegisterUser_Success");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("John Doe");
        user.setPhone("123456789");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                        .param("name", "John Doe")
                        .param("phone", "123456789")
                        .param("address", "123 Main St")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .param("isAdmin", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?success"));

        verify(userService, times(1)).registerUser(any(User.class));
        logger.info("testRegisterUser_Success completed successfully");
    }

    @Test
    void testRegisterUser_WithErrors() throws Exception {
    	logger.info("Running testRegisterUser_WithErrors");
        mockMvc.perform(post("/register")
                        .param("email", "") 
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
        logger.info("testRegisterUser_WithErrors completed successfully");
    }

    @Test
    void testShowLoginForm() throws Exception {
    	logger.info("Running testShowLoginForm");
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        logger.info("testShowLoginForm completed successfully");
    }

    @Test
    void testRedirectAfterLogin_AdminRole() throws Exception {
    	logger.info("Running testRedirectAfterLogin_AdminRole");
        var auth = new TestingAuthenticationToken("admin@example.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(get("/redirect").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
        logger.info("testRedirectAfterLogin_AdminRole completed successfully");
    }

    @Test
    void testRedirectAfterLogin_UserRole() throws Exception {
    	logger.info("Running testRedirectAfterLogin_UserRole");
        var auth = new TestingAuthenticationToken("user@example.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(get("/redirect").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));
        logger.info("testRedirectAfterLogin_UserRole completed successfully");
    }
}