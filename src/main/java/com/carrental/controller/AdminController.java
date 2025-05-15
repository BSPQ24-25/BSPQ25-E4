package com.carrental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import com.carrental.service.CarService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarService carService;
    
    @GetMapping("/dashboard")
    public String showAdminDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByEmail(username);

        if (user != null && user.getIsAdmin()) {
            model.addAttribute("adminName", user.getName());

            // Añade los datos que necesitas en el HTML
            long availableCars = carService.countAvailableCars(); 
            long activeRentals = bookingService.countActiveBookings();
            double totalRevenue = bookingService.getTotalRevenue();

            model.addAttribute("availableCars", availableCars);
            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("totalRevenue", totalRevenue);

            return "admin_dashboard"; 
        } else {
        return "redirect:/access-denied";
        }
    }


    @GetMapping("/users")
    public String userManagement(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-management";
    }

    @GetMapping("/reservations")
    public String pendingReservations(Model model) {
        List<Booking> pendingBookings = bookingService.getPendingBookings();
        model.addAttribute("pendingBookings", pendingBookings);
        return "admin/pending-reservations";
    }

    @GetMapping("/history")
    public String rentalHistory(Model model) {
        List<Booking> historyBookings = bookingService.getHistoryBookings();
        model.addAttribute("historyBookings", historyBookings);
        return "admin/rental-history";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/reservations/confirm")
    public String confirmBooking(@RequestParam("bookingId") Long bookingId) {
        bookingService.confirmBooking(bookingId); 
        return "redirect:/admin/reservations"; 
    }
}