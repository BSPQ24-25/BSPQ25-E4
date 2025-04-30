package com.carrental.service;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.BookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

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
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(3));
        booking.setUser(new User());
        booking.setCar(new Car());
        booking.setPaymentMethod("Credit Card");
        booking.setSecurityDeposit(100.0);
        booking.setRating(5);
        booking.setReview("Great");
    }

    @Test
    void getAllBookings_shouldReturnList() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllBookings();
        assertEquals(1, result.size());
    }

    @Test
    void getPendingBookings_shouldReturnPending() {
        when(bookingRepository.findByBookingStatus("pending")).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getPendingBookings();
        assertEquals("pending", result.get(0).getBookingStatus());
    }

    @Test
    void getHistoryBookings_shouldReturnHistory() {
        when(bookingRepository.findByBookingStatusIn(anyList()))
            .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getHistoryBookings();
        assertNotNull(result);
    }

    @Test
    void getBookingById_found() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Optional<Booking> result = bookingService.getBookingById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getBookingById_notFound() {
        when(bookingRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Booking> result = bookingService.getBookingById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void createBooking_shouldSave() {
        when(bookingRepository.save(booking)).thenReturn(booking);
        Booking result = bookingService.createBooking(booking);
        assertEquals(booking, result);
    }

    @Test
    void getUserRentalHistory_shouldReturnHistory() {
        when(bookingRepository.findByUserNameAndBookingStatusIn(anyString(), anyList()))
            .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getUserRentalHistory("test", List.of("confirmed"));
        assertEquals(1, result.size());
    }

    @Test
    void updateBooking_shouldUpdateIfExists() {
        Booking updated = new Booking();
        updated.setDailyPrice(70.0);
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusDays(2));
        updated.setBookingStatus("confirmed");
        updated.setPaymentMethod("Cash");
        updated.setSecurityDeposit(150.0);
        updated.setUser(new User());
        updated.setCar(new Car());
        updated.setRating(4);
        updated.setReview("Updated");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        Booking result = bookingService.updateBooking(1L, updated);

        assertEquals("confirmed", result.getBookingStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateBooking_shouldThrowIfNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.updateBooking(99L, booking));
    }

    @Test
    void deleteBooking_shouldDeleteIfExists() {
        when(bookingRepository.existsById(1L)).thenReturn(true);
        bookingService.deleteBooking(1L);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void deleteBooking_shouldThrowIfNotFound() {
        when(bookingRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> bookingService.deleteBooking(1L));
    }

    @Test
    void countActiveBookings_shouldReturnCount() {
        when(bookingRepository.countByBookingStatus("confirmed")).thenReturn(5L);
        assertEquals(5L, bookingService.countActiveBookings());
    }

    @Test
    void getTotalRevenue_shouldReturnSum() {
        when(bookingRepository.sumDailyPriceByBookingStatus("completed")).thenReturn(300.0);
        assertEquals(300.0, bookingService.getTotalRevenue());
    }

    @Test
    void getTotalRevenue_shouldReturnZeroIfNull() {
        when(bookingRepository.sumDailyPriceByBookingStatus("completed")).thenReturn(null);
        assertEquals(0.0, bookingService.getTotalRevenue());
    }

    @Test
    void confirmBooking_shouldUpdateStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.confirmBooking(1L);
        assertEquals("Confirmed", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBooking_shouldUpdateStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.cancelBooking(1L);
        assertEquals("Cancelled", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void completeBooking_shouldUpdateStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.completeBooking(1L);
        assertEquals("Completed", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void confirmBooking_shouldThrowIfNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.confirmBooking(99L));
    }
}