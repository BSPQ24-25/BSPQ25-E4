package com.carrental.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;

public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testShowAdminDashboard_AdminUser() throws Exception {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setIsAdmin(true);
        admin.setName("Admin");

        when(authentication.getName()).thenReturn("admin@example.com");
        when(userService.findByEmail("admin@example.com")).thenReturn(admin);

        mockMvc.perform(get("/admin/dashboard").principal(authentication))
               .andExpect(status().isOk())
               .andExpect(view().name("admin_dashboard"))
               .andExpect(model().attribute("adminName", "Admin"));
    }

    @Test
    public void testShowAdminDashboard_NonAdminUser() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setIsAdmin(false);

        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(user);

        mockMvc.perform(get("/admin/dashboard").principal(authentication))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/access-denied"));
    }

    @Test
    public void testUserManagement() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));

        mockMvc.perform(get("/admin/users"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/user-management"))
               .andExpect(model().attributeExists("users"));
    }

    @Test
    public void testPendingReservations() throws Exception {
        when(bookingService.getPendingBookings()).thenReturn(Arrays.asList(new Booking()));

        mockMvc.perform(get("/admin/reservations"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/pending-reservations"))
               .andExpect(model().attributeExists("pendingBookings"));
    }

    @Test
    public void testConfirmBooking() throws Exception {
        mockMvc.perform(post("/admin/reservations/confirm")
               .param("bookingId", "1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/reservations"));

        verify(bookingService).confirmBooking(1L);
    }
}