package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john@example.com");
    }

    @Test
    void userProfileTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userService.findByEmail("john@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/profile")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute("user", user));

        verify(userService, times(1)).findByEmail("john@example.com");
    }

    @Test
    void browseVehiclesTest() throws Exception {
        mockMvc.perform(get("/user/vehicles"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/browse-vehicles"));
    }

    @Test
    void bookingFormTest() throws Exception {
        mockMvc.perform(get("/user/bookings/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/booking-form"));
    }

    @Test
    void userRentalHistoryTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        Booking booking1 = new Booking();
        booking1.setBookingId(1L);
        Booking booking2 = new Booking();
        booking2.setBookingId(2L);

        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(userService.findByEmail("john@example.com")).thenReturn(user);
        when(bookingService.getUserRentalHistory("john@example.com", Arrays.asList("confirmed", "completed", "cancelled"))).thenReturn(bookings);

        mockMvc.perform(get("/history")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("user/rental-history"))
                .andExpect(model().attribute("historyBookings", bookings));

        verify(userService, times(1)).findByEmail("john@example.com");
        verify(bookingService, times(1)).getUserRentalHistory("john@example.com", Arrays.asList("confirmed", "completed", "cancelled"));
    }

    @Test
    void userDashboardTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userService.findByEmail("john@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/dashboard")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("user-dashboard"))
                .andExpect(model().attribute("userName", "John Doe"));

        verify(userService, times(1)).findByEmail("john@example.com");
    }

    @Test
    void cancelBookingTest() throws Exception {
        Long bookingId = 1L;

        doNothing().when(bookingService).cancelBooking(bookingId);

        mockMvc.perform(post("/user/history/cancel")
                        .param("bookingId", bookingId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/history"));

        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    @Test
    void completeBookingTest() throws Exception {
        Long bookingId = 1L;

        doNothing().when(bookingService).completeBooking(bookingId);

        mockMvc.perform(post("/user/history/complete")
                        .param("bookingId", bookingId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/history"));

        verify(bookingService, times(1)).completeBooking(bookingId);
    }
}