package com.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/profile")
    public String userProfile() {
        return "user/profile";
    }
    
    @GetMapping("/vehicles")
    public String browseVehicles() {
        return "user/browse-vehicles";
    }
    
    @GetMapping("/reservations")
    public String userReservations() {
        return "user/reservations";
    }
    
    @GetMapping("/history")
    public String userRentalHistory() {
        return "user/rental-history";
    }
}