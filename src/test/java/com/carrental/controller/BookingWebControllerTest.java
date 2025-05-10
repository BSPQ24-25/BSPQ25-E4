package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebMvcTest(BookingWebController.class)
public class BookingWebControllerTest {
	private static final Logger logger = LogManager.getLogger(BookingWebControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserRepository userRepository;

    private User mockUser;
    private Car mockCar;

    @BeforeEach
    public void setUp() {
    	logger.info("Setting up BookingWebControllerTest");
        mockUser = new User();
        mockUser.setEmail("user@example.com");
        mockUser.setPassword("password123");
        mockUser.setName("Test User");
        mockUser.setIsAdmin(false);

        mockCar = new Car();
        mockCar.setId(1L);
        mockCar.setBrand("Test Brand");
        mockCar.setModel("Test Model");
        mockCar.setColor("Red");
        logger.info("Mock user and car created");
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testCreateBooking_UserNotFound() throws Exception {
    	logger.info("Running testCreateBooking_UserNotFound");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "50.0")
                .param("rating", "4")
                .param("paymentMethod", "Credit Card")
                .param("review", "Good car")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error=user-not-found"));
        logger.info("User not found, redirection to login with error message");
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testCreateBooking_SuccessfulBooking() throws Exception {
    	logger.info("Running testCreateBooking_SuccessfulBooking");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        when(carService.getCarById(anyLong())).thenReturn(Optional.of(mockCar));

        when(bookingService.createBooking(any(Booking.class))).thenReturn(new Booking());

        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "50.0")
                .param("rating", "4")
                .param("paymentMethod", "Credit Card")
                .param("review", "Good car")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user/dashboard"));
        logger.info("Booking created successfully, redirection to dashboard");
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testCreateBooking_Admin() throws Exception {
    	logger.info("Running testCreateBooking_Admin");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        when(carService.getCarById(anyLong())).thenReturn(Optional.of(mockCar));

        when(bookingService.createBooking(any(Booking.class))).thenReturn(new Booking());

        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "60.0")
                .param("rating", "5")
                .param("paymentMethod", "PayPal")
                .param("review", "Excellent car")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user/dashboard"));
        logger.info("Admin booking created successfully, redirection to dashboard");
    }
}