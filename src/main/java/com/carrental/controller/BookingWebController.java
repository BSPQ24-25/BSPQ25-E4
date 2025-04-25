package com.carrental.controller;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/user/bookings")
public class BookingWebController {

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String showBookingForm(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "user/booking-form";

    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long carId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                @RequestParam double dailyPrice,@RequestParam int rating, @RequestParam String paymentMethod, @RequestParam String review,
                                Authentication authentication) {

        String email = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "redirect:/login?error=user-not-found";
        }
        User user = optionalUser.get();
        Car car = carService.getCarById(carId);

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setUser(user);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setBookingStatus("Pending");
        booking.setPaymentMethod(paymentMethod);
        booking.setDailyPrice(dailyPrice);
        booking.setSecurityDeposit(200.0);
        booking.setRating(rating);
        booking.setReview(review);
        bookingService.createBooking(booking);
        return "redirect:/user/dashboard";
    }
}
