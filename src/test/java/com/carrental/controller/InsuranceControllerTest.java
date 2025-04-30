package com.carrental.controller;

import com.carrental.models.Insurance;
import com.carrental.service.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

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
        List<Insurance> insurances = Arrays.asList(insurance);
        when(insuranceService.getAllInsurances()).thenReturn(insurances);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-management"))
                .andExpect(model().attributeExists("insurances"));
    }

    @Test
    void testEditInsurance() throws Exception {
        when(insuranceService.getInsuranceById(1L)).thenReturn(insurance);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/insurances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
    }

    @Test
    void testDeleteInsurance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/insurances/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).deleteInsurance(1L);
    }

    @Test
    void testCreateInsuranceForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/insurances/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/insurance-form"))
                .andExpect(model().attributeExists("insurance"));
    }

    @Test
    void testSaveInsurance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/insurances/save")
                        .flashAttr("insurance", insurance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/insurances"));

        verify(insuranceService, times(1)).saveInsurance(Mockito.any(Insurance.class));
    }

    @Test
    void testListUserInsurances() throws Exception {
        when(insuranceService.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/insurances"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/insurance-view"))
                .andExpect(model().attributeExists("insurances"));
    }
}