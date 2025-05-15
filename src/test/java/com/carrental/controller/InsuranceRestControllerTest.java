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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebMvcTest(InsuranceRestController.class)
class InsuranceRestControllerTest {
    
	private static final Logger logger = LogManager.getLogger(InsuranceRestControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    @Autowired
    private ObjectMapper objectMapper;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up InsuranceRestControllerTest");
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Test Provider");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(120.50);
        logger.info("Insurance object created with ID: " + insurance.getInsuranceId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateInsurance() throws Exception {
		logger.info("Running testCreateInsurance");
		
        doNothing().when(insuranceService).saveInsurance(any(Insurance.class));

        mockMvc.perform(post("/api/v1/insurances")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insurance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("Test Provider"))
                .andExpect(jsonPath("$.coverage").value("Full"))
                .andExpect(jsonPath("$.monthlyPrice").value(120.50));

        verify(insuranceService, times(1)).saveInsurance(any(Insurance.class));
        logger.info("Insurance created successfully");
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testGetAllInsurances() throws Exception {
    	        logger.info("Running testGetAllInsurances");
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/api/v1/insurances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].provider").value("Test Provider"));
        logger.info("All insurances retrieved successfully");
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testGetInsuranceById_Found() throws Exception {
    	logger.info("Running testGetInsuranceById_Found");
        when(insuranceService.getInsuranceById(1L)).thenReturn(insurance);

        mockMvc.perform(get("/api/v1/insurances/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("Test Provider"));
        logger.info("Insurance with ID 1 retrieved successfully");
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testGetInsuranceById_NotFound() throws Exception {
    	logger.info("Running testGetInsuranceById_NotFound");
        when(insuranceService.getInsuranceById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/insurances/2"))
                .andExpect(status().isNotFound());
        logger.info("Insurance with ID 2 not found");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteInsurance() throws Exception {
		logger.info("Running testDeleteInsurance");
		
        doNothing().when(insuranceService).deleteInsurance(1L);

        mockMvc.perform(delete("/api/v1/insurances/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(insuranceService, times(1)).deleteInsurance(1L);
        logger.info("Insurance with ID 1 deleted successfully");
    }
}