package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingWebControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingWebController bookingWebController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingWebController)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                .build();
    }

    @Test
    void testShowBookingForm() throws Exception {
        when(carService.getAllCars()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/bookings/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/booking-form"))
                .andExpect(model().attributeExists("cars"));

        verify(carService, times(1)).getAllCars();
    }

    @Test
    void testCreateBooking() throws Exception {
        Long carId = 1L;
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 7);
        double dailyPrice = 100.0;
        int rating = 5;
        String paymentMethod = "CREDIT_CARD";
        String review = "Excellent car!";

        User user = new User();
        user.setEmail("test@example.com");

        Car car = new Car();
        car.setId(carId);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setUser(user);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setBookingStatus("Pending");
        booking.setPaymentMethod(paymentMethod);
        booking.setDailyPrice(dailyPrice);
        booking.setSecurityDeposit(200.0);
        booking.setRating(rating);
        booking.setReview(review);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(carService.getCarById(carId)).thenReturn(car);

        mockMvc.perform(post("/user/bookings/create")
                        .param("carId", carId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .param("dailyPrice", String.valueOf(dailyPrice))
                        .param("rating", String.valueOf(rating))
                        .param("paymentMethod", paymentMethod)
                        .param("review", review))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));

        verify(bookingService, times(1)).createBooking(booking);
    }

    @Test
    void testCreateBooking_UserNotFound() throws Exception {
        Long carId = 1L;
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 7);
        double dailyPrice = 100.0;
        int rating = 5;
        String paymentMethod = "CREDIT_CARD";
        String review = "Excellent car!";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/user/bookings/create")
                        .param("carId", carId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .param("dailyPrice", String.valueOf(dailyPrice))
                        .param("rating", String.valueOf(rating))
                        .param("paymentMethod", paymentMethod)
                        .param("review", review))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=user-not-found"));

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}