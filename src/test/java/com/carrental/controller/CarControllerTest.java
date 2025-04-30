package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    void addCar_asAdmin_returnsCar() throws Exception {
        when(carService.addCar(any(Car.class))).thenReturn(testCar);

        mockMvc.perform(post("/cars")
                .param("admin", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    @Test
    void addCar_asUser_throwsException() throws Exception {
        mockMvc.perform(post("/cars")
                .param("admin", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCar)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllCars_returnsList() throws Exception {
        when(carService.getAllCars()).thenReturn(List.of(testCar));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testCar.getId()));
    }

    @Test
    void getCarById_returnsCar() throws Exception {
        when(carService.getCarById(1L)).thenReturn(testCar);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteCar_asAdmin_deletesSuccessfully() throws Exception {
        doNothing().when(carService).deleteCarById(1L);

        mockMvc.perform(delete("/cars/1").param("admin", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCar_asUser_throwsException() throws Exception {
        mockMvc.perform(delete("/cars/1").param("admin", "false"))
                .andExpect(status().isInternalServerError());
    }
}