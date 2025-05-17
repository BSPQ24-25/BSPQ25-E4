package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import com.carrental.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BookingControllerTest {

    private static final Logger logger = LogManager.getLogger(BookingControllerTest.class);
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private CarService carService;


    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        logger.info("Setting up BookingControllerTest");
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        logger.info("MockMvc setup complete");
    }

    private Booking createBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setDailyPrice(100.0);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(3));
        booking.setBookingStatus("PENDING");
        booking.setSecurityDeposit(200);
        booking.setPaymentMethod("CARD");

        Car car = new Car();
        car.setId(1L);
        booking.setCar(car);

        User user = new User();
        user.setId(1L);
        booking.setUser(user);
        return booking;
    }

    @Test
    void testGetAllBookings() throws Exception {
        Booking booking = createBooking();

        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1))
                .andExpect(jsonPath("$[0].dailyPrice").value(100.0));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetBookingById() throws Exception {
        Booking booking = createBooking();

        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.dailyPrice").value(100.0));

        verify(bookingService, times(1)).getBookingById(1L);
    }

    @Test
    void testGetBookingById_NotFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingById(1L);
    }

@Test
void testCreateBooking() throws Exception {
    logger.info("Running testCreateBooking");
    Booking booking = createBooking();

    // Simulamos lo que devolverían los servicios auxiliares
    when(userService.getUserById(3L)).thenReturn(Optional.of(booking.getUser()));
    when(carService.getCarById(2L)).thenReturn(Optional.of(booking.getCar()));
    when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

    String bookingJson = """
        {
            "dailyPrice": 100.0,
            "startDate": "2024-06-01",
            "endDate": "2024-06-04",
            "bookingStatus": "PENDING",
            "paymentMethod": "CARD",
            "securityDeposit": 200.0,
            "carId": 2,
            "userId": 3
        }
    """;

    mockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookingJson)
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bookingId").value(1))
        .andExpect(jsonPath("$.dailyPrice").value(100.0));

    verify(bookingService, times(1)).createBooking(any(Booking.class));
    logger.info("Booking created successfully with ID: " + booking.getBookingId());
}



    @Test
    void testUpdateBooking() throws Exception {
        Booking updatedBooking = createBooking();
        updatedBooking.setDailyPrice(120.0);

        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(updatedBooking);

        String updateJson = """
            {
                "dailyPrice": 120.0,
                "startDate": "2024-06-01",
                "endDate": "2024-06-04",
                "bookingStatus": "PENDING",
                "paymentMethod": "CARD",
                "securityDeposit": 200.0,
                "car": { "id": 2 },
                "user": { "id": 3 }
            }
        """;

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.dailyPrice").value(120.0));

        verify(bookingService, times(1)).updateBooking(eq(1L), any(Booking.class));
    }

    @Test
    void testDeleteBooking() throws Exception {
        Booking booking = createBooking();

        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).deleteBooking(1L);
    }

    @Test
    void testDeleteBooking_NotFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNotFound());

        verify(bookingService, never()).deleteBooking(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testConfirmBooking_Admin() throws Exception {
        Long bookingId = 1L;

        doNothing().when(bookingService).confirmBooking(bookingId);

        mockMvc.perform(post("/api/bookings/admin/bookings/confirm")
                        .param("bookingId", String.valueOf(bookingId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(bookingService).confirmBooking(bookingId);
    }
}
