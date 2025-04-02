package com.carrental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.CarService;
import com.carrental.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    // Mostrar usuarios
    @GetMapping("/users")
    public String userManagement(Model model) {
        // Obtener todos los usuarios desde el servicio
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users); // Pasar usuarios a la vista
        return "admin/user-management"; // Vista de gestión de usuarios
    }

    // Mostrar vehículos
    @GetMapping("/vehicles")
    public String vehicleManagement(Model model) {
        // Obtener todos los vehículos desde el servicio
        List<Car> vehicles = carService.getAllCars();
        model.addAttribute("vehicles", vehicles); // Pasar vehículos a la vista
        return "admin/vehicle-management"; // Vista de gestión de vehículos
    }

    // Mostrar reservas pendientes
    @GetMapping("/reservations")
    public String pendingReservations(Model model) {
        // Obtener todas las reservas pendientes desde el servicio
        List<Booking> pendingBookings = bookingService.getPendingBookings();
        model.addAttribute("pendingBookings", pendingBookings); // Pasar reservas pendientes a la vista
        return "admin/pending-reservations"; // Vista de reservas pendientes
    }

    // Mostrar historial de reservas
    @GetMapping("/history")
    public String rentalHistory(Model model) {
        // Obtener todas las reservas confirmadas, completadas o canceladas desde el servicio
        List<Booking> historyBookings = bookingService.getHistoryBookings();
        model.addAttribute("historyBookings", historyBookings); // Pasar historial de reservas a la vista
        return "admin/rental-history"; // Vista de historial de reservas
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

}
