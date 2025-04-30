package com.carrental.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class BookingTest {

    @Test
    void testBookingSettersAndGetters() {
        Booking booking = new Booking();
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        
        booking.setBookingId(1L);
        booking.setDailyPrice(30.0);
        booking.setStartDate(LocalDate.of(2025, 5, 1));
        booking.setEndDate(LocalDate.of(2025, 5, 10));
        booking.setBookingStatus("Confirmed");
        booking.setPaymentMethod("Credit Card");
        booking.setSecurityDeposit(150.0);
        booking.setUser(user);
        booking.setCar(car);
        booking.setRating(5);
        booking.setReview("Great car, smooth experience!");

        assertEquals(1L, booking.getBookingId());
        assertEquals(30.0, booking.getDailyPrice());
        assertEquals(LocalDate.of(2025, 5, 1), booking.getStartDate());
        assertEquals(LocalDate.of(2025, 5, 10), booking.getEndDate());
        assertEquals("Confirmed", booking.getBookingStatus());
        assertEquals("Credit Card", booking.getPaymentMethod());
        assertEquals(150.0, booking.getSecurityDeposit());
        assertNotNull(booking.getUser());
        assertEquals("John Doe", booking.getUser().getName());
        assertNotNull(booking.getCar());
        assertEquals("Toyota", booking.getCar().getBrand());
        assertEquals(5, booking.getRating());
        assertEquals("Great car, smooth experience!", booking.getReview());
    }

    @Test
    void testBookingUserRelationship() {
        Booking booking = new Booking();
        User user = new User();
        user.setName("Alice Smith");

        booking.setUser(user);

        assertNotNull(booking.getUser());
        assertEquals("Alice Smith", booking.getUser().getName());
    }

    @Test
    void testBookingCarRelationship() {
        Booking booking = new Booking();
        Car car = new Car();
        car.setBrand("Honda");

        booking.setCar(car);

        assertNotNull(booking.getCar());
        assertEquals("Honda", booking.getCar().getBrand());
    }

    @Test
    void testBookingDateValidity() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2025, 5, 1));
        booking.setEndDate(LocalDate.of(2025, 5, 10));

        assertTrue(booking.getStartDate().isBefore(booking.getEndDate()), "Start date should be before end date");
    }

    @Test
    void testBookingStatusUpdate() {
        Booking booking = new Booking();
        booking.setBookingStatus("Pending");

        booking.setBookingStatus("Confirmed");

        assertEquals("Confirmed", booking.getBookingStatus());
    }
}