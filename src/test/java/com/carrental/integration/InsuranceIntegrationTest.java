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
public class InsuranceIntegrationTest {
	private static final Logger logger = LogManager.getLogger(InsuranceIntegrationTest.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateInsurance() {
    	logger.info("Starting testCreateInsurance...");
        String insuranceJson = """
        {
            "provider": "SeguroTotal",
            "coverage": "Full",
            "monthlyPrice": 79.99
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(insuranceJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/insurances", request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        logger.info("Insurance created correctly: " + response.getBody());
    }
}
