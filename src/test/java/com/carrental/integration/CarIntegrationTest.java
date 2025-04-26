// 📁 src/test/java/com/carrental/integration/CarIntegrationTest.java
package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CarIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateCar() {
        String carDetails = """
        {
            "brand": "Ford",
            "model": "Fiesta",
            "color": "Blue",
            "fuelLevel": 85.5,
            "transmission": "Manual",
            "status": "AVAILABLE",
            "mileage": 40000,
            "manufacturingYear": 2020
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(carDetails, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/cars", request, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
}
