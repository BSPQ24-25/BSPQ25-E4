package com.carrental.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.BookingDTO;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.service.CarService;
import com.carrental.service.UserService;
import com.carrental.models.Booking;
import com.carrental.service.BookingService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking Controller", description = "API for managing bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(createdBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }




@PostMapping("/from-dto")
public ResponseEntity<Booking> createBookingFromDTO(@RequestBody BookingDTO dto) {
    try {
        User user = userService.getUserById(dto.getUserId()); // devuelve directamente User
        Car car = carService.getCarById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found")); // Optional<Car>

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setDailyPrice(dto.getDailyPrice());
        booking.setSecurityDeposit(dto.getSecurityDeposit());
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setPaymentMethod(dto.getPaymentMethod());
        booking.setBookingStatus(dto.getBookingStatus());

        Booking createdBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(createdBooking);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}




    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        try {
            Booking updatedBooking = bookingService.updateBooking(id, bookingDetails);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/bookings/confirm")
    public ResponseEntity<Void> confirmBooking(@RequestParam("bookingId") Long bookingId) {
        try {
            bookingService.confirmBooking(bookingId);
            return ResponseEntity.ok().build(); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}