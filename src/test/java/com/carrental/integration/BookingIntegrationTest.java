package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
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
}
