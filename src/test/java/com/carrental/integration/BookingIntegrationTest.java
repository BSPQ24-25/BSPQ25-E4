package com.carrental.integration;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;

import com.carrental.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class BookingIntegrationTest {

    private static final Logger logger = LogManager.getLogger(BookingIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    private Long testUserId;
    private Long testCarId;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        carRepository.deleteAll();

        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setAddress("123 Main St");
        user.setPhone("0612345678");
        user = userRepository.save(user);
        testUserId = user.getId();

        Car car = new Car();
        car.setBrand("TestBrand");
        car.setModel("TestModel");
        car.setManufacturingYear(2022);
        car.setPricePerDay(50.0);
        car = carRepository.save(car);
        testCarId = car.getId();
    }

    @Test
    public void testCreateBooking() {
        logger.info("Starting testCreateBooking...");

        String bookingDetails = String.format("""
            {
                "car": { "id": %d },
                "user": { "id": %d },
                "startDate": "2025-05-01",
                "endDate": "2025-05-07",
                "dailyPrice": 75.0,
                "bookingStatus": "PENDING",
                "paymentMethod": "CARD",
                "securityDeposit": 150.0,
                "rating": 4,
                "review": "All good!"
            }
            """, testCarId, testUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(bookingDetails, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/bookings", request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        logger.info("Booking created correctly: " + response.getBody());
    }
}
