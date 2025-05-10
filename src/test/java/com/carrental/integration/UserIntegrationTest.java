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
public class UserIntegrationTest {
	private static final Logger logger = LogManager.getLogger(UserIntegrationTest.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateUser() {
    	logger.info("Starting testCreateUser...");
        // Generar un email único para evitar conflictos por duplicados
        String uniqueEmail = "john" + System.currentTimeMillis() + "@example.com";

        String userJson = String.format("""
        {
            "name": "John Tester",
            "phone": "123456789",
            "email": "%s",
            "password": "secure123",
            "address": "123 Main St",
            "isAdmin": false
        }
        """, uniqueEmail);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(userJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/users", request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        logger.info("User created correctly: " + response.getBody());
    }
}
