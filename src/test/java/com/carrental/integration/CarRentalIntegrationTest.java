package com.carrental.integration;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import com.carrental.service.UserService;
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

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    void testUserBooksAndCancelsCar_thenCleansUp() {
        System.out.println("STARTING testUserBooksAndCancelsCar_thenCleansUp...");

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

        // 2. Crear coche
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("White");
        car.setFuelLevel(100.0);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(5000);
        car.setManufacturingYear(2020);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Car> carRequest = new HttpEntity<>(car, headers);
        ResponseEntity<Car> carResponse = restTemplate.postForEntity("/cars?admin=true", carRequest, Car.class);
        assertEquals(HttpStatus.OK, carResponse.getStatusCode());
        assertNotNull(carResponse.getBody());
        Long carId = carResponse.getBody().getId();
        assertNotNull(carId);

        // 3. Crear reserva
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(carResponse.getBody());
        booking.setDailyPrice(50.0);
        booking.setSecurityDeposit(150.0);
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(5));
        booking.setPaymentMethod("CARD");
        booking.setBookingStatus("Confirmed");

        HttpEntity<Booking> bookingRequest = new HttpEntity<>(booking, headers);
        ResponseEntity<Booking> bookingResponse = restTemplate.postForEntity("/api/bookings", bookingRequest, Booking.class);
        assertEquals(HttpStatus.OK, bookingResponse.getStatusCode());
        assertNotNull(bookingResponse.getBody());
        Long bookingId = bookingResponse.getBody().getBookingId();
        assertNotNull(bookingId);

        // 4. Verificar reserva
        ResponseEntity<Booking> fetched = restTemplate.getForEntity("/api/bookings/" + bookingId, Booking.class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        assertNotNull(fetched.getBody());
        assertEquals(userId, fetched.getBody().getUser().getId());
        assertEquals(carId, fetched.getBody().getCar().getId());

        // 5. Eliminar reserva
        restTemplate.delete("/api/bookings/" + bookingId);

        // 6. Eliminar coche
        restTemplate.delete("/cars/" + carId + "?admin=true");

        // 7. Eliminar usuario
        userService.deleteUser(userId);
    }
}
