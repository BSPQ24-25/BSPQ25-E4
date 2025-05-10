package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@ExtendWith(MockitoExtension.class)
class CarControllerTest {
	private static final Logger logger = LogManager.getLogger(CarControllerTest.class);
    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private ObjectMapper objectMapper;
    private Car testCar;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up CarControllerTest");
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        objectMapper = new ObjectMapper();

        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Corolla");
        logger.info("Test car created with ID: " + testCar.getId());
    }

    @Test
    void addCar_asAdmin_shouldReturnCar() throws Exception {
		logger.info("Running addCar_asAdmin_shouldReturnCar test");
		
        when(carService.addCar(any(Car.class))).thenReturn(testCar);

        mockMvc.perform(post("/cars")
                .param("admin", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("Toyota"));
        logger.info("Car added successfully");
    }

    @Test
    void addCar_asUser_shouldReturnInternalServerError() throws Exception {
		logger.info("Running addCar_asUser_shouldReturnInternalServerError test");
		
        mockMvc.perform(post("/cars")
                .param("admin", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isInternalServerError());
        logger.info("Car addition failed for non-admin user");
    }

    @Test
    void addCar_missingAdminParam_shouldReturnBadRequest() throws Exception {
    	logger.info("Running addCar_missingAdminParam_shouldReturnBadRequest test");
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isBadRequest());
        logger.info("Car addition failed due to missing admin parameter");
    }

    @Test
    void getAllCars_shouldReturnListOfCars() throws Exception {
    	logger.info("Running getAllCars_shouldReturnListOfCars test");
        when(carService.getAllCars()).thenReturn(List.of(testCar));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].model").value("Corolla"));
        logger.info("All cars retrieved successfully");
    }

    @Test
    void getCarById_shouldReturnCar() throws Exception {
    	logger.info("Running getCarById_shouldReturnCar test");
        when(carService.getCarById(1L)).thenReturn(Optional.of(testCar));

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        logger.info("Car with ID 1 retrieved successfully");
    }

    @Test
    void getCarById_whenNotFound_shouldReturnNotFound() throws Exception {
		logger.info("Running getCarById_whenNotFound_shouldReturnNotFound test");
		
        when(carService.getCarById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cars/99"))
                .andExpect(status().isNotFound());
        logger.info("Car with ID 99 not found");
    }

    @Test
    void deleteCar_asUser_shouldReturnInternalServerError() throws Exception {
    	logger.info("Running deleteCar_asUser_shouldReturnInternalServerError test");
        mockMvc.perform(delete("/cars/1")
                .param("admin", "false"))
                .andExpect(status().isInternalServerError());
        logger.info("Car deletion failed for non-admin user");
    }

    @Test
    void deleteCar_missingAdminParam_shouldReturnBadRequest() throws Exception {
    	logger.info("Running deleteCar_missingAdminParam_shouldReturnBadRequest test");
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isBadRequest());
        logger.info("Car deletion failed due to missing admin parameter");
    }
}