package com.carrental.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;

@Controller
@RequestMapping("/user")
public class RentalHistoryController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/history")
    public String showRentalHistory(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
           
            List<Booking> historyBookings = bookingService.getUserRentalHistory(
                user.getEmail(), 
                List.of("confirmed", "completed", "cancelled")
            );
             
            model.addAttribute("historyBookings", historyBookings);
        }
        
        return "user/rental-history";
    }
}