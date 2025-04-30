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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setIsAdmin(false);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .param("isAdmin", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?success"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_WithErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "email", "password"));

        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testRedirectAfterLogin_AdminRole() throws Exception {
        var auth = new TestingAuthenticationToken("admin@example.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        mockMvc.perform(get("/redirect").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void testRedirectAfterLogin_UserRole() throws Exception {
        var auth = new TestingAuthenticationToken("user@example.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        mockMvc.perform(get("/redirect").principal(auth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));
    }
}