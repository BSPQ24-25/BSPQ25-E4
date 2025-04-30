package com.carrental.controller;

import com.carrental.models.Insurance;
import com.carrental.service.InsuranceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceRestController.class)
class InsuranceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    @Autowired
    private ObjectMapper objectMapper;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Test Provider");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(120.50);
    }

    @Test
    void testCreateInsurance() throws Exception {
        doNothing().when(insuranceService).saveInsurance(any(Insurance.class));

        mockMvc.perform(post("/api/v1/insurances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insurance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("Test Provider"))
                .andExpect(jsonPath("$.coverage").value("Full"))
                .andExpect(jsonPath("$.monthlyPrice").value(120.50));

        verify(insuranceService, times(1)).saveInsurance(any(Insurance.class));
    }

    @Test
    void testGetAllInsurances() throws Exception {
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/api/v1/insurances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].provider").value("Test Provider"));
    }

    @Test
    void testGetInsuranceById_Found() throws Exception {
        when(insuranceService.getInsuranceById(1L)).thenReturn(insurance);

        mockMvc.perform(get("/api/v1/insurances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("Test Provider"));
    }

    @Test
    void testGetInsuranceById_NotFound() throws Exception {
        when(insuranceService.getInsuranceById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/insurances/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInsurance() throws Exception {
        doNothing().when(insuranceService).deleteInsurance(1L);

        mockMvc.perform(delete("/api/v1/insurances/1"))
                .andExpect(status().isNoContent());

        verify(insuranceService, times(1)).deleteInsurance(1L);
    }
}