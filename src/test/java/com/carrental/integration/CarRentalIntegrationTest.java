package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.models.User;
import com.carrental.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class CarRentalIntegrationTest {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CarRentalIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    void testUserBooksAndCancelsCar_thenCleansUp() {
        logger.info("🚗 Starting full car rental integration test...");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 1. Crear usuario
        User user = new User();
        user.setName("Alice");
        user.setPhone("123456789");
        user.setEmail("alice+" + System.currentTimeMillis() + "@example.com");
        user.setPassword("password123");
        user.setAddress("123 Main St");
        user.setIsAdmin(false);
        user = userService.registerUser(user);
        assertNotNull(user.getId());
        Long userId = user.getId();

        // 2. Crear seguro
        Insurance insurance = new Insurance();
        insurance.setProvider("SafeDrive");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(85.0);

        HttpEntity<Insurance> insuranceEntity = new HttpEntity<>(insurance, headers);
        ResponseEntity<Insurance> insuranceResponse = restTemplate.postForEntity("/api/v1/insurances", insuranceEntity, Insurance.class);
        assertEquals(HttpStatus.OK, insuranceResponse.getStatusCode());
        Insurance savedInsurance = insuranceResponse.getBody();
        assertNotNull(savedInsurance);

        // 3. Crear coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Yaris");
        car.setColor("Blue");
        car.setFuelLevel(95.0);
        car.setTransmission("Manual");
        car.setStatus("Available");
        car.setMileage(12000);
        car.setManufacturingYear(2021);
        car.setInsuranceID(savedInsurance);

        HttpEntity<Car> carEntity = new HttpEntity<>(car, headers);
        ResponseEntity<Car> carResponse = restTemplate.postForEntity("/api/cars?admin=true", carEntity, Car.class);
        assertEquals(HttpStatus.OK, carResponse.getStatusCode());
        Car savedCar = carResponse.getBody();
        assertNotNull(savedCar);
        Long carId = savedCar.getId();

        // 4. Crear booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(savedCar);
        booking.setDailyPrice(49.99);
        booking.setSecurityDeposit(200.0);
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(3));
        booking.setBookingStatus("Confirmed");
        booking.setPaymentMethod("Credit Card");

        HttpEntity<Booking> bookingEntity = new HttpEntity<>(booking, headers);
        ResponseEntity<Booking> bookingResponse = restTemplate.postForEntity("/api/bookings", bookingEntity, Booking.class);
        assertEquals(HttpStatus.OK, bookingResponse.getStatusCode());
        Booking savedBooking = bookingResponse.getBody();
        assertNotNull(savedBooking);
        Long bookingId = savedBooking.getBookingId();

        // 5. Verificar booking
        ResponseEntity<Booking> fetched = restTemplate.getForEntity("/api/bookings/" + bookingId, Booking.class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());
        assertEquals(userId, fetched.getBody().getUser().getId());
        assertEquals(carId, fetched.getBody().getCar().getId());

        // 6. Eliminar booking
        restTemplate.delete("/api/bookings/" + bookingId);

        // 7. Eliminar coche
        restTemplate.exchange("/api/cars/" + carId + "?admin=true", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        // 8. Eliminar seguro
        restTemplate.delete("/api/v1/insurances/" + savedInsurance.getId());

        // 9. Eliminar usuario
        userService.deleteUser(userId);

        logger.info("✅ Car rental integration test completed.");
    }
}
