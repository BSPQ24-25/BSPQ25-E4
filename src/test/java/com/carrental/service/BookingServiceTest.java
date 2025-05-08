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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BookingServiceTest {
	
	private static final Logger logger = LogManager.getLogger(BookingServiceTest.class);
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
    	logger.info("Starting test configuration for BookingService");
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
        logger.info("Configuration completed");
    }

    @Test
    void getAllBookings_shouldReturnList() {
    	logger.info("Executing test: getAllBookings_shouldReturnList");
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllBookings();
        assertEquals(1, result.size());
        logger.info("Test getAllBookings_shouldReturnList completed successfully");
    }

    @Test
    void getPendingBookings_shouldReturnPending() {
    	logger.info("Executing test: getPendingBookings_shouldReturnPending");
        when(bookingRepository.findByBookingStatus("pending")).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getPendingBookings();
        assertEquals("pending", result.get(0).getBookingStatus());
        logger.info("Test getPendingBookings_shouldReturnPending completed successfully");
    }

    @Test
    void getHistoryBookings_shouldReturnHistory() {
		logger.info("Executing test: getHistoryBookings_shouldReturnHistory");
        when(bookingRepository.findByBookingStatusIn(anyList()))
            .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getHistoryBookings();
        assertNotNull(result);
        logger.info("Test getHistoryBookings_shouldReturnHistory completed successfully");
    }

    @Test
    void getBookingById_found() {
    	logger.info("Executing test: getBookingById_found");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Optional<Booking> result = bookingService.getBookingById(1L);
        assertTrue(result.isPresent());
        logger.info("Test getBookingById_found completed successfully");
    }

    @Test
    void getBookingById_notFound() {
    	logger.info("Executing test: getBookingById_notFound");
        when(bookingRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Booking> result = bookingService.getBookingById(2L);
        assertFalse(result.isPresent());
        logger.info("Test getBookingById_notFound completed successfully");
    }

    @Test
    void createBooking_shouldSave() {
		logger.info("Executing test: createBooking_shouldSave");
		
        when(bookingRepository.save(booking)).thenReturn(booking);
        Booking result = bookingService.createBooking(booking);
        assertEquals(booking, result);
        logger.info("Test createBooking_shouldSave completed successfully");
    }

    @Test
    void getUserRentalHistory_shouldReturnHistory() {
		logger.info("Executing test: getUserRentalHistory_shouldReturnHistory");
		
        when(bookingRepository.findByUserNameAndBookingStatusIn(anyString(), anyList()))
            .thenReturn(List.of(booking));
        List<Booking> result = bookingService.getUserRentalHistory("test", List.of("confirmed"));
        assertEquals(1, result.size());
        logger.info("Test getUserRentalHistory_shouldReturnHistory completed successfully");
    }

    @Test
    void updateBooking_shouldUpdateIfExists() {
    	        logger.info("Executing test: updateBooking_shouldUpdateIfExists");
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
        logger.info("Test updateBooking_shouldUpdateIfExists completed successfully");
    }

    @Test
    void updateBooking_shouldThrowIfNotFound() {
    	        logger.info("Executing test: updateBooking_shouldThrowIfNotFound");
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.updateBooking(99L, booking));
        logger.info("Test updateBooking_shouldThrowIfNotFound completed successfully");
    }
    

    @Test
    void deleteBooking_shouldDeleteIfExists() {
    	logger.info("Executing test: deleteBooking_shouldDeleteIfExists");
        when(bookingRepository.existsById(1L)).thenReturn(true);
        bookingService.deleteBooking(1L);
        verify(bookingRepository).deleteById(1L);
        logger.info("Test deleteBooking_shouldDeleteIfExists completed successfully");
    }

    @Test
    void deleteBooking_shouldThrowIfNotFound() {
    	logger.info("Executing test: deleteBooking_shouldThrowIfNotFound");
        when(bookingRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> bookingService.deleteBooking(1L));
        logger.info("Test deleteBooking_shouldThrowIfNotFound completed successfully");
    }

    @Test
    void countActiveBookings_shouldReturnCount() {
		logger.info("Executing test: countActiveBookings_shouldReturnCount");
		
        when(bookingRepository.countByBookingStatus("confirmed")).thenReturn(5L);
        assertEquals(5L, bookingService.countActiveBookings());
        logger.info("Test countActiveBookings_shouldReturnCount completed successfully");
    }

    @Test
    void getTotalRevenue_shouldReturnSum() {
		logger.info("Executing test: getTotalRevenue_shouldReturnSum");
		
        when(bookingRepository.sumDailyPriceByBookingStatus("completed")).thenReturn(300.0);
        assertEquals(300.0, bookingService.getTotalRevenue());
        logger.info("Test getTotalRevenue_shouldReturnSum completed successfully");
    }

    @Test
    void getTotalRevenue_shouldReturnZeroIfNull() {
    	logger.info("Executing test: getTotalRevenue_shouldReturnZeroIfNull");
        when(bookingRepository.sumDailyPriceByBookingStatus("completed")).thenReturn(null);
        assertEquals(0.0, bookingService.getTotalRevenue());
        logger.info("Test getTotalRevenue_shouldReturnZeroIfNull completed successfully");
    }

    @Test
    void confirmBooking_shouldUpdateStatus() {
    	logger.info("Executing test: confirmBooking_shouldUpdateStatus");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.confirmBooking(1L);
        assertEquals("Confirmed", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
        logger.info("Test confirmBooking_shouldUpdateStatus completed successfully");
    }

    @Test
    void cancelBooking_shouldUpdateStatus() {
    	logger.info("Executing test: cancelBooking_shouldUpdateStatus");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.cancelBooking(1L);
        assertEquals("Cancelled", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
        logger.info("Test cancelBooking_shouldUpdateStatus completed successfully");
    }

    @Test
    void completeBooking_shouldUpdateStatus() {
    	logger.info("Executing test: completeBooking_shouldUpdateStatus");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        bookingService.completeBooking(1L);
        assertEquals("Completed", booking.getBookingStatus());
        verify(bookingRepository).save(booking);
        logger.info("Test completeBooking_shouldUpdateStatus completed successfully");
    }

    @Test
    void confirmBooking_shouldThrowIfNotFound() {
    	logger.info("Executing test: confirmBooking_shouldThrowIfNotFound");
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookingService.confirmBooking(99L));
        logger.info("Test confirmBooking_shouldThrowIfNotFound completed successfully");
    }
}