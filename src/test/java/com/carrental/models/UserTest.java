package com.carrental.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class UserTest {

    @Test
    void testUserSettersAndGetters() {
        User user = new User();
        
        user.setId(1L);
        user.setName("John Doe");
        user.setPhone("1234567890");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setAddress("123 Main St");
        user.setIsAdmin(true);
        List<Booking> bookings = new ArrayList<>();
        user.setBookings(bookings);

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("1234567890", user.getPhone());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("123 Main St", user.getAddress());
        assertTrue(user.getIsAdmin());
        assertNotNull(user.getBookings());
        assertEquals(0, user.getBookings().size());
    }

    @Test
    void testUserAdminStatus() {
        User user = new User();
        
        user.setIsAdmin(true);

        assertTrue(user.getIsAdmin(), "User should be admin");

        user.setIsAdmin(false);

        assertFalse(user.getIsAdmin(), "User should not be admin");
    }

    @Test
    void testUserBookings() {
        User user = new User();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        user.setBookings(bookings);

        List<Booking> userBookings = user.getBookings();

        assertNotNull(userBookings);
        assertEquals(2, userBookings.size());
        assertTrue(userBookings.contains(booking1));
        assertTrue(userBookings.contains(booking2));
    }

    @Test
    void testUserEmailUnique() {
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        assertNotEquals(user1.getEmail(), user2.getEmail(), "Emails should be unique");
    }
}