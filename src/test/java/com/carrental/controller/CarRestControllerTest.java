package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarRestController.class)
class CarRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private ObjectMapper objectMapper;
    private Car testCar;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Camry");
    }

    @Test
    void getAllCars_shouldReturnList() throws Exception {
        when(carService.getAllCars()).thenReturn(List.of(testCar));

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testCar.getId()))
                .andExpect(jsonPath("$[0].brand").value("Toyota"));
    }

    @Test
    void createCar_shouldReturnCreatedCar() throws Exception {
        when(carService.addCar(any(Car.class))).thenReturn(testCar);

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.model").value("Camry"));
    }

    @Test
    void getCarById_found_shouldReturnCar() throws Exception {
        when(carService.getCarById(1L)).thenReturn(testCar);

        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    @Test
    void getCarById_notFound_shouldReturn404() throws Exception {
        when(carService.getCarById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCarById_shouldReturnNoContent() throws Exception {
        doNothing().when(carService).deleteCarById(1L);

        mockMvc.perform(delete("/api/v1/cars/1"))
                .andExpect(status().isNoContent());
    }
}