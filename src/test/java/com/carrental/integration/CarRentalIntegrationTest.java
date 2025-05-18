package com.carrental.integration;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.models.User;
import com.carrental.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import com.carrental.config.TestSecurityConfig;
import com.carrental.dto.BookingDTO;

import org.springframework.context.annotation.Import;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CarRentalIntegrationTest {
	private static final Logger logger = LogManager.getLogger(CarRentalIntegrationTest.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    void testUserBooksAndCancelsCar_thenCleansUp() {
        logger.info("Starting integration test for car rental system...");

        // 1. Crear usuario
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

        // 2. Crear insurance
        Insurance insurance = new Insurance();
        
        insurance.setProvider("TestInsurance");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(80.0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Insurance> insuranceRequest = new HttpEntity<>(insurance, headers);
        ResponseEntity<Insurance> insuranceResponse = restTemplate.postForEntity("/api/v1/insurances", insuranceRequest, Insurance.class);
        assertEquals(HttpStatus.OK, insuranceResponse.getStatusCode());
        Insurance createdInsurance = insuranceResponse.getBody();
        assertNotNull(createdInsurance);

        // 3. Crear coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("White");
        car.setFuelLevel(100.0);
        car.setTransmission("Automatic");
        car.setStatus("AVAILABLE");
        car.setMileage(5000);
        car.setManufacturingYear(2020);
        car.setInsuranceID(createdInsurance);

        HttpEntity<Car> carRequest = new HttpEntity<>(car, headers);
        ResponseEntity<Car> carResponse = restTemplate.postForEntity("/api/cars?admin=true", carRequest, Car.class);
        assertEquals(HttpStatus.OK, carResponse.getStatusCode());
        assertNotNull(carResponse.getBody());
        Long carId = carResponse.getBody().getId();
        assertNotNull(carId);

        // 4. Crear booking DTO
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(userId);
        bookingDTO.setCarId(carId);
        bookingDTO.setDailyPrice(50.0);
        bookingDTO.setSecurityDeposit(150.0);
        bookingDTO.setStartDate(LocalDate.now().plusDays(1));
        bookingDTO.setEndDate(LocalDate.now().plusDays(5));
        bookingDTO.setPaymentMethod("CARD");
        bookingDTO.setBookingStatus("CONFIRMED");

        HttpEntity<BookingDTO> bookingRequest = new HttpEntity<>(bookingDTO, headers);
        ResponseEntity<Booking> bookingResponse = restTemplate.postForEntity("/api/bookings", bookingRequest, Booking.class);

        if (bookingResponse.getStatusCode() != HttpStatus.OK) {
            fail("Booking creation failed with status: " + bookingResponse.getStatusCode());
        }

        Long bookingId = bookingResponse.getBody().getBookingId();
        assertNotNull(bookingId);

        // 5. Obtener booking
        ResponseEntity<Booking> fetched = restTemplate.getForEntity("/api/bookings/" + bookingId, Booking.class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());
        assertEquals(userId, fetched.getBody().getUser().getId());
        assertEquals(carId, fetched.getBody().getCar().getId());

        // 6. Eliminar booking
        restTemplate.delete("/api/bookings/" + bookingId);

        // 7. Eliminar coche
        HttpEntity<Void> deleteEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteCarResponse = restTemplate.exchange(
                "/api/cars/" + carId + "?admin=true",
                HttpMethod.DELETE,
                deleteEntity,
                Void.class
        );
        assertEquals(HttpStatus.OK, deleteCarResponse.getStatusCode());

        // 8. Eliminar insurance
        restTemplate.delete("/api/v1/insurances/" + createdInsurance.getId());

        // 9. Eliminar usuario
        userService.deleteUser(userId);

        logger.info("Integration test completed successfully.");
    }

}
