

package com.carrental.integration;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;

import com.carrental.service.UserService;

import ch.qos.logback.classic.Logger;

import org.apache.logging.log4j.LogManager;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import com.carrental.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CarRentalIntegrationTest {
	private static final org.apache.logging.log4j.Logger logger = (org.apache.logging.log4j.Logger) LogManager.getLogger(TestSecurityConfig.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    void testUserBooksAndCancelsCar_thenCleansUp() {
        
        logger.info("Starting integration test for car rental system...");
        // 1. Create user
        logger.info("Creating user...");
        User user = new User();
        user.setName("Alice");
        user.setPhone("123456789");
        user.setEmail("alice+" + System.currentTimeMillis() + "@example.com");
        user.setPassword("password123");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);
        user = userService.registerUser(user);
        Long userId = user.getId();
        assertNotNull(userId);
        logger.info("User created with ID: " + userId);

        // 2. Create car
        logger.info("Creating car...");
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("White");
        car.setFuelLevel(100.0);
        car.setTransmission("Automatic");
        car.setStatus("AVAILABLE");
        car.setMileage(5000);
        car.setManufacturingYear(2020);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Car> carRequest = new HttpEntity<>(car, headers);
        ResponseEntity<Car> carResponse = restTemplate.postForEntity("/cars?admin=true", carRequest, Car.class);
        assertEquals(HttpStatus.OK, carResponse.getStatusCode());
        Long carId = carResponse.getBody().getId();
        assertNotNull(carId);
        logger.info("Car created with ID: " + carId);

        // 3. Create booking
        logger.info("Creating booking...");
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(carResponse.getBody());


        booking.setDailyPrice(50.0);
        booking.setSecurityDeposit(150.0);
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(5));
        booking.setPaymentMethod("CARD");
        booking.setBookingStatus("CONFIRMED");

        HttpEntity<Booking> bookingRequest = new HttpEntity<>(booking, headers);
        ResponseEntity<Booking> bookingResponse = restTemplate.postForEntity("/api/bookings", bookingRequest, Booking.class);
        assertEquals(HttpStatus.OK, bookingResponse.getStatusCode());
        Long bookingId = bookingResponse.getBody().getBookingId();
        assertNotNull(bookingId);
        logger.info("Booking created with ID: " + bookingId);

        // 4. Verify booking
        logger.info("Verifying booking...");
        ResponseEntity<Booking> fetched = restTemplate.getForEntity("/api/bookings/" + bookingId, Booking.class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertEquals(userId, fetched.getBody().getUser().getId());
        assertEquals(carId, fetched.getBody().getCar().getId());
        logger.info("Booking verified successfully.");

        // 5. Delete booking
        logger.info("Deleting booking...");
        restTemplate.delete("/api/bookings/" + bookingId);
        logger.info("Booking deleted.");

        // 6. Delete car
        logger.info("Deleting car...");
        restTemplate.delete("/cars/" + carId + "?admin=true");
        logger.info("Car deleted.");

        // 7. Delete user
        logger.info("Deleting user...");
        userService.deleteUser(userId);
        logger.info("User deleted.");

        logger.info("Integration test completed successfully.");
    }
}
