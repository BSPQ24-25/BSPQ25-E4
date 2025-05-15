package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.service.CarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarApiControllerTest {
	private static final Logger logger = LogManager.getLogger(CarApiControllerTest.class);
    private MockMvc mockMvc;
    private CarService carService;
    private CarApiController controller;

    private Car testCar;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up CarApiControllerTest");
        carService = Mockito.mock(CarService.class);

        controller = new CarApiController();
        ReflectionTestUtils.setField(controller, "carService", carService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Camry");
        testCar.setColor("Blue");
        testCar.setFuelLevel(75.0);
        testCar.setTransmission("Automatic");
        testCar.setStatus("Available");
        testCar.setMileage(10000);
        testCar.setManufacturingYear(2020);
        Insurance insurance = new Insurance();
        insurance.setInsuranceId(123L);
        testCar.setInsuranceID(insurance);
        logger.info("Test car created with ID: " + testCar.getId());
    }

    @Test
    void getAllCars_shouldReturnListOfCars() throws Exception {
    	logger.info("Running getAllCars_shouldReturnListOfCars test");
        Car car1 = new Car(); car1.setId(1L); car1.setModel("Model X");
        Car car2 = new Car(); car2.setId(2L); car2.setModel("Model Y");

        List<Car> cars = Arrays.asList(car1, car2);
        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Model X"))
                .andExpect(jsonPath("$[1].model").value("Model Y"));
        logger.info("All cars retrieved successfully");
    }

    @Test
    void searchCarsByField_shouldReturnMatchingCars() throws Exception {
		logger.info("Running searchCarsByField_shouldReturnMatchingCars test");
		
        when(carService.searchByField("brand", "Toyota")).thenReturn(List.of(testCar));

        mockMvc.perform(get("/api/cars/search")
                        .param("field", "brand")
                        .param("value", "Toyota"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[0].model").value("Camry"));
        logger.info("Cars filtered by brand successfully");
    }

    @Test
    void searchCarsByTwoFields_shouldReturnFilteredCars() throws Exception {
    	logger.info("Running searchCarsByTwoFields_shouldReturnFilteredCars test");
        when(carService.searchByField("brand", "Toyota")).thenReturn(List.of(testCar));

        mockMvc.perform(get("/api/cars/search")
                        .param("field", "brand")
                        .param("value", "Toyota")
                        .param("field2", "model")
                        .param("value2", "Camry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Camry"));
        logger.info("Cars filtered by brand and model successfully");
    }

    @Test
    void searchCarsByTwoFields_withNoMatch_shouldReturnEmptyList() throws Exception {
    	logger.info("Running searchCarsByTwoFields_withNoMatch_shouldReturnEmptyList test");
        when(carService.searchByField("brand", "Toyota")).thenReturn(List.of(testCar));

        mockMvc.perform(get("/api/cars/search")
                        .param("field", "brand")
                        .param("value", "Toyota")
                        .param("field2", "model")
                        .param("value2", "Civic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        logger.info("No cars found matching the criteria");
    }

    @Test
    void searchCars_withMissingField_shouldReturnBadRequest() throws Exception {
    	logger.info("Running searchCars_withMissingField_shouldReturnBadRequest test");
        mockMvc.perform(get("/api/cars/search")
                        .param("value", "Toyota"))
                .andExpect(status().isBadRequest());
        logger.info("Bad request due to missing field parameter");
    }

    @Test
    void getAllCars_whenServiceFails_shouldReturnServerError() throws Exception {
    	logger.info("Running getAllCars_whenServiceFails_shouldReturnServerError test");
        when(carService.getAllCars()).thenThrow(new RuntimeException("Database down"));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isInternalServerError());
        logger.info("Server error occurred while retrieving cars");
    }
}
