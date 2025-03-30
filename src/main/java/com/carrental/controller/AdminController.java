package com.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public String userManagement() {
        return "admin/user-management";
    }
    
    @GetMapping("/vehicles")
    public String vehicleManagement() {
        return "admin/vehicle-management";
    }
    
    @GetMapping("/reservations")
    public String pendingReservations() {
        return "admin/pending-reservations";
    }
    
    @GetMapping("/history")
    public String rentalHistory() {
        return "admin/rental-history";
    }
}