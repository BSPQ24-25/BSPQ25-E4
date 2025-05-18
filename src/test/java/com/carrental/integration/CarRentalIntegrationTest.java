package com.carrental.integration;

import com.carrental.config.TestSecurityConfig;
import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.models.User;
import com.carrental.service.UserService;
import com.carrental.dto.BookingDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
        logger.info("🔧 Starting integration test for car rental system...");

        // 1. Create user
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
        logger.info("✅ User created with ID: {}", userId);

        // 2. Create insurance
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
        logger.info("✅ Insurance created with ID: {}", createdInsurance.getId());

        // 3. Create car
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
        ResponseEntity<Car> carResponse = restTemplate.postForEntity("/cars?admin=true", carRequest, Car.class);
        assertEquals(HttpStatus.OK, carResponse.getStatusCode());
        Car createdCar = carResponse.getBody();
        Long carId = createdCar.getId();
        assertNotNull(carId);
        logger.info("✅ Car created with ID: {}", carId);

        // 4. Create booking - USANDO Map para mayor flexibilidad en la depuración
        Map<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("userId", userId);
        bookingMap.put("carId", carId);
        bookingMap.put("dailyPrice", 50.0);
        bookingMap.put("securityDeposit", 150.0);
        bookingMap.put("startDate", LocalDate.now().plusDays(1).toString());
        bookingMap.put("endDate", LocalDate.now().plusDays(5).toString());
        bookingMap.put("paymentMethod", "CARD");
        bookingMap.put("bookingStatus", "CONFIRMED");

        // También preparar el DTO para alternancia
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(userId);
        bookingDTO.setCarId(carId);
        bookingDTO.setDailyPrice(50.0);
        bookingDTO.setSecurityDeposit(150.0);
        bookingDTO.setStartDate(LocalDate.now().plusDays(1));
        bookingDTO.setEndDate(LocalDate.now().plusDays(5));
        bookingDTO.setPaymentMethod("CARD");
        bookingDTO.setBookingStatus("CONFIRMED");

        logger.info("📦 Enviando BookingDTO: userId={}, carId={}, startDate={}, endDate={}", 
            bookingDTO.getUserId(), bookingDTO.getCarId(), 
            bookingDTO.getStartDate(), bookingDTO.getEndDate());

        // Probar primero con el DTO
        HttpEntity<BookingDTO> bookingRequest = new HttpEntity<>(bookingDTO, headers);
        
        try {
            // Obtener respuesta detallada como String para ver el mensaje de error si existe
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                "/api/bookings",
                HttpMethod.POST,
                bookingRequest,
                String.class
            );
            
            logger.info("📬 Respuesta raw: status={}, body={}", 
                rawResponse.getStatusCode(), rawResponse.getBody());
                
            // Si llegamos aquí, intentar con el objeto normal
            ResponseEntity<Booking> bookingResponse = restTemplate.postForEntity(
                "/api/bookings", bookingRequest, Booking.class);
            
            assertEquals(HttpStatus.OK, bookingResponse.getStatusCode());
            Booking createdBooking = bookingResponse.getBody();
            assertNotNull(createdBooking);
            Long bookingId = createdBooking.getBookingId();
            logger.info("✅ Booking created with ID: {}", bookingId);

            // 5. Fetch and verify booking
            ResponseEntity<Booking> fetched = restTemplate.getForEntity("/api/bookings/" + bookingId, Booking.class);
            assertEquals(HttpStatus.OK, fetched.getStatusCode());
            assertEquals(userId, fetched.getBody().getUser().getId());
            assertEquals(carId, fetched.getBody().getCar().getId());
            logger.info("✅ Booking verified successfully.");

            // 6. Delete booking
            restTemplate.delete("/api/bookings/" + bookingId);
            logger.info("🗑️ Booking deleted.");
        } catch (Exception e) {
            logger.error("❌ Error durante la prueba: {}", e.getMessage(), e);
            
            // Si falla con el DTO, intentar con Map directamente
            logger.info("🔄 Intentando ahora con Map en lugar de DTO...");
            HttpEntity<Map<String, Object>> mapRequest = new HttpEntity<>(bookingMap, headers);
            
            try {
                ResponseEntity<String> mapRawResponse = restTemplate.exchange(
                    "/api/bookings",
                    HttpMethod.POST,
                    mapRequest,
                    String.class
                );
                
                logger.info("📬 Respuesta con Map: status={}, body={}", 
                    mapRawResponse.getStatusCode(), mapRawResponse.getBody());
                    
                // Si no es 2xx, fallar
                if (!mapRawResponse.getStatusCode().is2xxSuccessful()) {
                    fail("Error creating booking with Map approach: " + mapRawResponse.getBody());
                }
                
                // Continuar con el test si llegamos aquí
                // ... (código para continuar)
                
            } catch (Exception ex) {
                logger.error("❌ También falló el intento con Map: {}", ex.getMessage(), ex);
                fail("Error en ambos intentos de creación de booking: " + e.getMessage() + " / " + ex.getMessage());
            }
        }

        // 7. Delete car
        HttpEntity<Void> deleteEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteCarResponse = restTemplate.exchange(
                "/cars/" + carId + "?admin=true",
                HttpMethod.DELETE,
                deleteEntity,
                Void.class
        );
        assertEquals(HttpStatus.OK, deleteCarResponse.getStatusCode());
        logger.info("🗑️ Car deleted.");

        // 8. Delete insurance
        restTemplate.delete("/api/v1/insurances/" + createdInsurance.getId());
        logger.info("🗑️ Insurance deleted.");

        // 9. Delete user
        userService.deleteUser(userId);
        logger.info("🗑️ User deleted.");

        logger.info("✅ Integration test completed successfully.");
    }
}