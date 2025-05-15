package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class BookingIntegrationTest {
	private static final Logger logger = LogManager.getLogger(BookingIntegrationTest.class);
    @Autowired
    private TestRestTemplate restTemplate;
/*  MAL???
    @Test
    public void testCreateBooking() {
    	logger.info("Starting testCreateBooking...");
        String bookingDetails = """
        {
            "car": { "id": 1 },
            "user": { "id": 1 },
            "startDate": "2025-05-01",
            "endDate": "2025-05-07",
            "dailyPrice": 75.0,
            "bookingStatus": "PENDING",
            "paymentMethod": "CARD",
            "securityDeposit": 150.0,
            "rating": 4,
            "review": "All good!"
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(bookingDetails, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/bookings", request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        logger.info("Booking created correctly: " + response.getBody());
    }
    */
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private Car car;
    private User user;

    @BeforeEach
    public void setup() {
        bookingRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();

        car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Blue");
        car.setFuelLevel(75.5);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(15000);
        car.setManufacturingYear(2022);
        car = carRepository.save(car);

        user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setPhone("123456789");
        user.setAddress("123 Main Street");
        user.setIsAdmin(false);
        user = userRepository.save(user);
    }

    @Test
    public void testCreateBooking() {
        String bookingJson = String.format("""
        {
            "user": { "id": %d },
            "car": { "id": %d },
            "startDate": "%s",
            "endDate": "%s",
            "dailyPrice": 49.99,
            "bookingStatus": "PENDING",
            "paymentMethod": "Credit Card",
            "securityDeposit": 150.0,
            "rating": 4,
            "review": "Everything went well!"
        }
        """,
                user.getId(),
                car.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(bookingJson, headers);

        ResponseEntity<Booking> response = restTemplate.postForEntity("/api/bookings", request, Booking.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Credit Card", response.getBody().getPaymentMethod());
        assertEquals(49.99, response.getBody().getDailyPrice());
    }
    }
