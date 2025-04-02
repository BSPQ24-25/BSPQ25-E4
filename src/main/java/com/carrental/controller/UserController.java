package com.carrental.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.carrental.models.Booking;
import com.carrental.service.BookingService;

@Controller
public class UserController {

    @Autowired
    private BookingService bookingService;

    // Este método está en conflicto con el AuthController, por eso se comenta o elimina.
    // @GetMapping("/profile")
    // public String userProfile() {
    //     return "user/profile";
    // }

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
        String userName = authentication.getName();  // Obtiene el 'name' del usuario autenticado
        List<String> statuses = Arrays.asList("confirmed", "completed", "cancelled");  // Los estados de la reserva
        List<Booking> historyBookings = bookingService.getUserRentalHistory(userName, statuses);
        model.addAttribute("historyBookings", historyBookings);  // Agregar los bookings al modelo
        return "user/rental-history";  // Retorna la vista
    }
}
