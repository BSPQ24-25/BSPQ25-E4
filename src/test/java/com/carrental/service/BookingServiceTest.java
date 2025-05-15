package com.carrental.service;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentGatewayService paymentGatewayService;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus("pending");
        booking.setDailyPrice(50.0);
        booking.setPaymentMethod("credit card");
        booking.setUser(new User()); 
        booking.setCar(new Car());
    }

    @Test
    void createBooking_shouldSaveAndProcessPayment() {
        when(paymentGatewayService.processPayment(booking)).thenReturn("Processed via Credit Card (simulated)");
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertEquals(booking, result);
        verify(paymentGatewayService).processPayment(booking);
        verify(bookingRepository).save(booking);
    }
}