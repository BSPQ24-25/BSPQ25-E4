package com.carrental.controller;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.carrental.models.Booking;
import com.carrental.models.User;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;

public class AdminControllerTest {

    private UserService userService;
    private BookingService bookingService;
    private Authentication authentication;
    private Model model;
    private AdminController adminController;

    private User adminUser;
    private User regularUser;
    private List<User> userList;
    private List<Booking> bookingList;

    public void setup() {
        userService = mock(UserService.class);
        bookingService = mock(BookingService.class);
        authentication = mock(Authentication.class);
        model = mock(Model.class);
        
        adminController = new AdminController();
        try {
            java.lang.reflect.Field userServiceField = AdminController.class.getDeclaredField("userService");
            userServiceField.setAccessible(true);
            userServiceField.set(adminController, userService);
            
            java.lang.reflect.Field bookingServiceField = AdminController.class.getDeclaredField("bookingService");
            bookingServiceField.setAccessible(true);
            bookingServiceField.set(adminController, bookingService);
        } catch (Exception e) {
            System.err.println("Error setting up mocks: " + e.getMessage());
        }
        
        // Setup test data
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@example.com");
        adminUser.setIsAdmin(true);

        regularUser = new User();
        regularUser.setId(2L);
        regularUser.setName("Regular User");
        regularUser.setEmail("user@example.com");
        regularUser.setIsAdmin(false);

        userList = new ArrayList<>();
        userList.add(adminUser);
        userList.add(regularUser);

        bookingList = new ArrayList<>();
        Booking booking = new Booking();
        booking.setBookingId(1L);
        bookingList.add(booking);
    }

    public void testShowAdminDashboard_WithAdminUser_ReturnsAdminDashboard() {
        setup();
        
        when(authentication.getName()).thenReturn("admin@example.com");
        when(userService.findByEmail("admin@example.com")).thenReturn(adminUser);

        String viewName = adminController.showAdminDashboard(authentication, model);

        assert viewName.equals("admin_dashboard") : "Expected 'admin_dashboard' but got " + viewName;
        verify(model).addAttribute("adminName", "Admin User");
    }

    public void testShowAdminDashboard_WithNonAdminUser_RedirectsToAccessDenied() {
        setup();
        
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(regularUser);

        String viewName = adminController.showAdminDashboard(authentication, model);

        assert viewName.equals("redirect:/access-denied") : 
            "Expected 'redirect:/access-denied' but got " + viewName;
    }

    public void testUserManagement_ReturnsUserManagementView() {
        setup();
        
        when(userService.getAllUsers()).thenReturn(userList);
        
        String viewName = adminController.userManagement(model);
        
        assert viewName.equals("admin/user-management") : 
            "Expected 'admin/user-management' but got " + viewName;
        verify(model).addAttribute("users", userList);
    }

    public void testPendingReservations_ReturnsPendingReservationsView() {
        setup();
        
        when(bookingService.getPendingBookings()).thenReturn(bookingList);

        String viewName = adminController.pendingReservations(model);
        
        assert viewName.equals("admin/pending-reservations") : 
            "Expected 'admin/pending-reservations' but got " + viewName;
        verify(model).addAttribute("pendingBookings", bookingList);
    }

    public void testRentalHistory_ReturnsRentalHistoryView() {
        setup();
        
        when(bookingService.getHistoryBookings()).thenReturn(bookingList);
        
        String viewName = adminController.rentalHistory(model);
        
        assert viewName.equals("admin/rental-history") : 
            "Expected 'admin/rental-history' but got " + viewName;
        verify(model).addAttribute("historyBookings", bookingList);
    }

    public void testEditUserForm_ReturnsEditUserView() {
        setup();
        
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(adminUser);
        
        String viewName = adminController.editUserForm(userId, model);
        
        assert viewName.equals("admin/edit-user") : 
            "Expected 'admin/edit-user' but got " + viewName;
        verify(model).addAttribute("user", adminUser);
    }

    public void testUpdateUser_RedirectsToUsers() {
        setup();
        
        Long userId = 1L;
        
        String viewName = adminController.updateUser(userId, adminUser);
        
        assert viewName.equals("redirect:/admin/users") : 
            "Expected 'redirect:/admin/users' but got " + viewName;
        verify(userService).updateUser(userId, adminUser);
    }

    public void testDeleteUser_RedirectsToUsers() {
        setup();
        
        Long userId = 1L;
        
        String viewName = adminController.deleteUser(userId);
        
        assert viewName.equals("redirect:/admin/users") : 
            "Expected 'redirect:/admin/users' but got " + viewName;
        verify(userService).deleteUser(userId);
    }

    public void testConfirmBooking_RedirectsToPendingReservations() {
        setup();
        
        Long bookingId = 1L;
        
        String viewName = adminController.confirmBooking(bookingId);
        
        assert viewName.equals("redirect:/admin/reservations") : 
            "Expected 'redirect:/admin/reservations' but got " + viewName;
        verify(bookingService).confirmBooking(bookingId);
    }
}