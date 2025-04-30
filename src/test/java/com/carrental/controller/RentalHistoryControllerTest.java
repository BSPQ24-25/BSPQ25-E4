package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RentalHistoryControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentalHistoryController rentalHistoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalHistoryController).build();
    }

    @Test
    void showRentalHistoryTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        Booking booking1 = new Booking();
        booking1.setBookingId(1L);
        Booking booking2 = new Booking();
        booking2.setBookingId(2L);

        List<Booking> bookings = Arrays.asList(booking1, booking2);


        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        when(bookingService.getUserRentalHistory("john@example.com", Arrays.asList("confirmed", "completed", "cancelled")))
            .thenReturn(bookings);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john@example.com");

        mockMvc.perform(get("/user/history")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("user/rental-history"))
                .andExpect(model().attribute("historyBookings", bookings));

        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(bookingService, times(1)).getUserRentalHistory("john@example.com", Arrays.asList("confirmed", "completed", "cancelled"));
    }
}