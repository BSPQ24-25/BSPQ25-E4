package com.carrental.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;

@Controller
public class UserController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/vehicles")
    public String browseVehicles() {
        return "user/browse-vehicles";
    }

    @GetMapping("/reservations")
    public String userReservations() {
        return "user/reservations";
    }

    @GetMapping("/history")
    public String userRentalHistory(Authentication authentication, Model model) {
        String userName = authentication.getName();
        List<String> statuses = Arrays.asList("confirmed", "completed", "cancelled");
        List<Booking> historyBookings = bookingService.getUserRentalHistory(userName, statuses);
        model.addAttribute("historyBookings", historyBookings);
        return "user/rental-history";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("userName", user.getName());
        return "user-dashboard";
    }
}
