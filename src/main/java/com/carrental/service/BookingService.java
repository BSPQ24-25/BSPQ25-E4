package com.carrental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrental.models.Booking;
import com.carrental.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByBookingStatus("pending");
    }

    // Obtener historial de reservas (estados "confirmed", "completed", "cancelled")
    public List<Booking> getHistoryBookings() {
        return bookingRepository.findByBookingStatusIn(List.of("confirmed", "completed", "cancelled"));
    }

    // Get a booking by its ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserRentalHistory(String userName, List<String> statuses) {
        return bookingRepository.findByUserNameAndBookingStatusIn(userName, statuses);
    }

    // Update an existing booking
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setDailyPrice(bookingDetails.getDailyPrice());
            booking.setStartDate(bookingDetails.getStartDate());
            booking.setEndDate(bookingDetails.getEndDate());
            booking.setBookingStatus(bookingDetails.getBookingStatus());
            booking.setPaymentMethod(bookingDetails.getPaymentMethod());
            booking.setSecurityDeposit(bookingDetails.getSecurityDeposit());
            booking.setUser(bookingDetails.getUser());
            booking.setCar(bookingDetails.getCar());
            booking.setRating(bookingDetails.getRating());
            booking.setReview(bookingDetails.getReview());
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Booking not found");
        }
    }

    // Delete a booking by its ID
    public void deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Booking not found with id: " + id);
        }
    }
}
