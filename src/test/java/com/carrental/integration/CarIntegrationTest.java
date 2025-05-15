// 📁 src/test/java/com/carrental/integration/CarIntegrationTest.java
package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
import com.carrental.models.Car;
import com.carrental.repository.CarRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CarIntegrationTest {
	private static final Logger logger = LogManager.getLogger(CarIntegrationTest.class);
    @Autowired
    private TestRestTemplate restTemplate;
/* MAL??
    @Test
    public void testCreateCar() {
    	logger.info("Starting testCreateCar...");
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

      //MAL??  ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/cars", request, String.class);
        ResponseEntity<String> response = restTemplate.postForEntity("/cars", request, String.class);
        restTemplate.postForEntity("/cars?admin=true", request, String.class);

        assertEquals(200, response.getStatusCodeValue());
        logger.info("Car created correctly: " + response.getBody());
    }
    */
    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    public void setup() {
        carRepository.deleteAll();
    }

    @Test
    public void testCreateCarWithAdminTrue() {
        Car car = new Car();
        car.setBrand("Ford");
        car.setModel("Focus");
        car.setColor("Red");
        car.setFuelLevel(80.0);
        car.setTransmission("Manual");
        car.setStatus("Available");
        car.setMileage(30000);
        car.setManufacturingYear(2021);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Car> request = new HttpEntity<>(car, headers);

        ResponseEntity<Car> response = restTemplate.postForEntity("/cars?admin=true", request, Car.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ford", response.getBody().getBrand());
        assertEquals("Focus", response.getBody().getModel());
    }

    @Test
    public void testCreateCarWithAdminFalseShouldFail() {
        Car car = new Car();
        car.setBrand("Nissan");
        car.setModel("Micra");
        car.setColor("White");
        car.setFuelLevel(60.0);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(10000);
        car.setManufacturingYear(2022);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Car> request = new HttpEntity<>(car, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/cars?admin=false", request, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Only admins can add cars"));
    }
}
