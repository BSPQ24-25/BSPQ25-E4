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
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

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
        testCar.setModel("Corolla");
    }

    @Test
    void addCar_asAdmin_shouldReturnCar() throws Exception {
        when(carService.addCar(any(Car.class))).thenReturn(testCar);

        mockMvc.perform(post("/cars")
                .param("admin", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    @Test
    void addCar_asUser_shouldReturnInternalServerError() throws Exception {
        mockMvc.perform(post("/cars")
                .param("admin", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addCar_missingAdminParam_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCars_shouldReturnListOfCars() throws Exception {
        when(carService.getAllCars()).thenReturn(List.of(testCar));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].model").value("Corolla"));
    }

    @Test
    void getCarById_shouldReturnCar() throws Exception {
        when(carService.getCarById(1L)).thenReturn(testCar);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCarById_whenNotFound_shouldReturnNotFound() throws Exception {
        when(carService.getCarById(99L)).thenThrow(new NoSuchElementException("Car not found"));

        mockMvc.perform(get("/cars/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_asAdmin_shouldReturnOk() throws Exception {
        doNothing().when(carService).deleteCarById(1L);

        mockMvc.perform(delete("/cars/1").param("admin", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCar_asUser_shouldReturnInternalServerError() throws Exception {
        mockMvc.perform(delete("/cars/1").param("admin", "false"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteCar_missingAdminParam_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isBadRequest());
    }
}