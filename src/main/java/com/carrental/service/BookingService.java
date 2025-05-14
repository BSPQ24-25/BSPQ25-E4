package com.carrental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.repository.BookingRepository;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentGatewayService paymentGatewayService;

    @Autowired
    private UserService userService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PaymentGatewayService paymentGatewayService) {
        this.bookingRepository = bookingRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByBookingStatus("pending");
    }

    public List<Booking> getHistoryBookings() {
        return bookingRepository.findByBookingStatusIn(List.of("confirmed", "completed", "cancelled"));
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        Booking saved = bookingRepository.save(booking);
        
        String result = paymentGatewayService.processPayment(saved);
        System.out.println(result);

        return saved;
    }

    public List<Booking> getUserRentalHistory(String email, List<String> statuses) {
        User user = userService.findByEmail(email);
        return bookingRepository.findByUserAndBookingStatusIn(user, statuses);
    }

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

    public void deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Booking not found with id: " + id);
        }
    }

    public long countActiveBookings() {
        return bookingRepository.countByBookingStatus("confirmed");
    }

    public double getTotalRevenue() {
        Double total = bookingRepository.sumDailyPriceByBookingStatus("completed");
        return total != null ? total : 0.0;
    }

    public void confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus("Confirmed");
        bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus("Cancelled");
        bookingRepository.save(booking);
    }

    public void completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus("Completed");
        bookingRepository.save(booking);
    }
}