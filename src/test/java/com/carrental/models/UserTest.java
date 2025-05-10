package com.carrental.models;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class UserTest {
	private static final Logger logger = LogManager.getLogger(UserTest.class);
    @Test
    void testUserSettersAndGetters() {
    	logger.info("Testing User getters and setters");
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
        logger.info("User getters and setters tested successfully");
    }

    @Test
    void testUserAdminStatus() {
    	logger.info("Testing User admin status");
        User user = new User();
        
        user.setIsAdmin(true);

        assertTrue(user.getIsAdmin(), "User should be admin");

        user.setIsAdmin(false);

        assertFalse(user.getIsAdmin(), "User should not be admin");
        logger.info("User admin status tested successfully");
    }

    @Test
    void testUserBookings() {
    	logger.info("Testing User bookings");
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
        logger.info("User bookings tested successfully");
    }

    @Test
    void testUserEmailUnique() {
    	logger.info("Testing User email uniqueness");
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        assertNotEquals(user1.getEmail(), user2.getEmail(), "Emails should be unique");
        logger.info("User email uniqueness tested successfully");
    }
}