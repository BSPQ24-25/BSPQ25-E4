package com.carrental.controller;

import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @Autowired
    private CarService carService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        long availableCars = carService.countAvailableCars();
        model.addAttribute("availableCars", availableCars);
        return "admin_dashboard";
    }
}
