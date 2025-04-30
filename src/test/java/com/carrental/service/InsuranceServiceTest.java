package com.carrental.service;

import com.carrental.models.Insurance;
import com.carrental.repository.InsuranceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {

    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private InsuranceService insuranceService;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Liberty Seguros");
        insurance.setCoverage("Cobertura completa");
        insurance.setMonthlyPrice(49.99);
        insurance.setCar(Collections.emptyList());
    }

    @Test
    void getAllInsurances_returnsList() {
        when(insuranceRepository.findAll()).thenReturn(List.of(insurance));

        List<Insurance> result = insuranceService.getAllInsurances();

        assertEquals(1, result.size());
        assertEquals("Liberty Seguros", result.get(0).getProvider());
        verify(insuranceRepository).findAll();
    }

    @Test
    void saveInsurance_savesSuccessfully() {
        insuranceService.saveInsurance(insurance);

        verify(insuranceRepository).save(insurance);
    }

    @Test
    void getInsuranceById_found() {
        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));

        Insurance result = insuranceService.getInsuranceById(1L);

        assertNotNull(result);
        assertEquals("Cobertura completa", result.getCoverage());
        verify(insuranceRepository).findById(1L);
    }

    @Test
    void getInsuranceById_notFound_returnsNull() {
        when(insuranceRepository.findById(1L)).thenReturn(Optional.empty());

        Insurance result = insuranceService.getInsuranceById(1L);

        assertNull(result);
        verify(insuranceRepository).findById(1L);
    }

    @Test
    void deleteInsurance_callsRepositoryDelete() {
        doNothing().when(insuranceRepository).deleteById(1L);

        insuranceService.deleteInsurance(1L);

        verify(insuranceRepository).deleteById(1L);
    }
}