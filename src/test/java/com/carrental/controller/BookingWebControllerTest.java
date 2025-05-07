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

@WebMvcTest(BookingWebController.class) // Esto indica que solo probaremos el controlador
public class BookingWebControllerTest {

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
        // Preparar datos mockeados para las pruebas
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
    }

    @Test
    public void testCreateBooking_UserNotFound() throws Exception {
        // Simulamos que el usuario no existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Intentamos realizar la reserva con el usuario no encontrado
        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "50.0")
                .param("rating", "4")
                .param("paymentMethod", "Credit Card")
                .param("review", "Good car")
                .with(csrf())) // Simula que el usuario está autenticado
            .andExpect(status().is3xxRedirection()) // Espera una redirección (3xx)
            .andExpect(redirectedUrl("/login?error=user-not-found")); // Verifica que redirige al login con el mensaje de error
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testCreateBooking_SuccessfulBooking() throws Exception {
        // Simulamos que el usuario existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        // Simulamos que el coche existe
        when(carService.getCarById(anyLong())).thenReturn(Optional.of(mockCar));

        // Simulamos la creación del booking
        when(bookingService.createBooking(any(Booking.class))).thenReturn(new Booking());

        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "50.0")
                .param("rating", "4")
                .param("paymentMethod", "Credit Card")
                .param("review", "Good car")
                .with(csrf())) // Necesario si CSRF está habilitado
            .andExpect(status().is3xxRedirection()) // Espera una redirección (3xx)
            .andExpect(redirectedUrl("/user/dashboard")); // Verifica que redirige al dashboard
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testCreateBooking_Admin() throws Exception {
        // Simulamos que el usuario es admin y existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        // Simulamos que el coche existe
        when(carService.getCarById(anyLong())).thenReturn(Optional.of(mockCar));

        // Simulamos la creación del booking
        when(bookingService.createBooking(any(Booking.class))).thenReturn(new Booking());

        mockMvc.perform(post("/user/bookings/create")
                .param("carId", "1")
                .param("startDate", "2025-05-10")
                .param("endDate", "2025-05-15")
                .param("dailyPrice", "60.0")
                .param("rating", "5")
                .param("paymentMethod", "PayPal")
                .param("review", "Excellent car")
                .with(csrf())) // Necesario si CSRF está habilitado
            .andExpect(status().is3xxRedirection()) // Espera una redirección (3xx)
            .andExpect(redirectedUrl("/user/dashboard")); // Verifica que redirige al dashboard
    }
}