package com.carrental.controller;

import com.carrental.models.Insurance;
import com.carrental.service.InsuranceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {
	private static final Logger logger = LogManager.getLogger(InsuranceControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up InsuranceControllerTest");
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Test Provider");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(99.99);
        logger.info("Insurance object created with ID: " + insurance.getInsuranceId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testListAdminInsurances() throws Exception {
		logger.info("Running testListAdminInsurances");
		
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/admin/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-management"))
                .andExpect(model().attributeExists("insurances"));
        logger.info("Admin insurances listed successfully");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditInsurance_found() throws Exception {
    	logger.info("Running testEditInsurance_found");
        when(insuranceService.getInsuranceById(1L)).thenReturn(insurance);

        mockMvc.perform(get("/admin/insurances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
        logger.info("Insurance edit form displayed successfully");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditInsurance_notFound() throws Exception {
    	logger.info("Running testEditInsurance_notFound");
        when(insuranceService.getInsuranceById(999L)).thenReturn(null);

        mockMvc.perform(get("/admin/insurances/edit/999"))
                .andExpect(status().isNotFound());
        logger.info("Insurance not found for edit");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteInsurance_success() throws Exception {
    	logger.info("Running testDeleteInsurance_success");
        mockMvc.perform(post("/admin/insurances/delete/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).deleteInsurance(1L);
        logger.info("Insurance deleted successfully");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteInsurance_notFound() throws Exception {
    	logger.info("Running testDeleteInsurance_notFound");
        doThrow(new IllegalArgumentException("Not found")).when(insuranceService).deleteInsurance(999L);

        mockMvc.perform(post("/admin/insurances/delete/999")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
        logger.info("Insurance not found for deletion");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateInsuranceForm() throws Exception {
    	logger.info("Running testCreateInsuranceForm");
        mockMvc.perform(get("/admin/insurances/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
        logger.info("Insurance creation form displayed successfully");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveInsurance_valid() throws Exception {
		logger.info("Running testSaveInsurance_valid");
	
        mockMvc.perform(post("/admin/insurances/save")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("insurance", insurance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).saveInsurance(any(Insurance.class));
        logger.info("Insurance saved successfully");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSaveInsurance_withValidationErrors() throws Exception {
    	logger.info("Running testSaveInsurance_withValidationErrors");
        Insurance invalidInsurance = new Insurance();

        mockMvc.perform(post("/admin/insurances/save")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("insurance", invalidInsurance))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"));
        logger.info("Validation errors occurred while saving insurance");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testListUserInsurances() throws Exception {
    	logger.info("Running testListUserInsurances");
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/user/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/insurance-view"))
                .andExpect(model().attributeExists("insurances"));
        logger.info("User insurances listed successfully");
    }
}