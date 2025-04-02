package com.carrental.controller;

import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.carrental.service.BookingService;


@Controller
public class AdminDashboardController {

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        long availableCars = carService.countAvailableCars();
        long activeRentals = bookingService.countActiveBookings();
        double totalRevenue = bookingService.getTotalRevenue();

        model.addAttribute("availableCars", availableCars);
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin_dashboard";
    }
}


