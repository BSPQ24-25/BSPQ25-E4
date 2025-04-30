package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void testGetAllBookings() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setDailyPrice(100.0);

        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1))
                .andExpect(jsonPath("$[0].dailyPrice").value(100.0));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setDailyPrice(100.0);

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
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setDailyPrice(100.0);

        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyPrice\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.dailyPrice").value(100.0));

        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }

    @Test
    void testUpdateBooking() throws Exception {
        Booking updatedBooking = new Booking();
        updatedBooking.setBookingId(1L);
        updatedBooking.setDailyPrice(120.0);

        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyPrice\":120.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.dailyPrice").value(120.0));

        verify(bookingService, times(1)).updateBooking(eq(1L), any(Booking.class));
    }

    @Test
    void testDeleteBooking() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1L);

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

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setBookingStatus("PENDING");

        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(bookingService).confirmBooking(bookingId);

        mockMvc.perform(post("/api/bookings/admin/bookings/confirm")
                        .param("bookingId", String.valueOf(bookingId))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/bookings/pending"));

        verify(bookingService).confirmBooking(bookingId);
    }
}