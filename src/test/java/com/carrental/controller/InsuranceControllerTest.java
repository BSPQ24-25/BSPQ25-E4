package com.carrental.controller;

import com.carrental.models.Insurance;
import com.carrental.service.InsuranceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsuranceController.class)
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceService insuranceService;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Test Provider");
        insurance.setCoverage("Full");
        insurance.setMonthlyPrice(99.99);
    }

    @Test
    void testListAdminInsurances() throws Exception {
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/admin/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-management"))
                .andExpect(model().attributeExists("insurances"));
    }

    @Test
    void testEditInsurance_found() throws Exception {
        when(insuranceService.getInsuranceById(1L)).thenReturn(insurance);

        mockMvc.perform(get("/admin/insurances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
    }

    @Test
    void testEditInsurance_notFound() throws Exception {
        when(insuranceService.getInsuranceById(999L)).thenReturn(null);

        mockMvc.perform(get("/admin/insurances/edit/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInsurance_success() throws Exception {
        mockMvc.perform(post("/admin/insurances/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).deleteInsurance(1L);
    }

    @Test
    void testDeleteInsurance_notFound() throws Exception {
        doThrow(new IllegalArgumentException("Not found")).when(insuranceService).deleteInsurance(999L);

        mockMvc.perform(post("/admin/insurances/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateInsuranceForm() throws Exception {
        mockMvc.perform(get("/admin/insurances/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
    }

    @Test
    void testSaveInsurance_valid() throws Exception {
        mockMvc.perform(post("/admin/insurances/save")
                        .flashAttr("insurance", insurance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).saveInsurance(any(Insurance.class));
    }

    @Test
    void testSaveInsurance_withValidationErrors() throws Exception {
        Insurance invalidInsurance = new Insurance();

        mockMvc.perform(post("/admin/insurances/save")
                        .flashAttr("insurance", invalidInsurance))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"));
    }

    @Test
    void testListUserInsurances() throws Exception {
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/user/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/insurance-view"))
                .andExpect(model().attributeExists("insurances"));
    }
}