package com.carrental.service;

import com.carrental.models.Insurance;
import com.carrental.repository.InsuranceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class InsuranceServiceTest {
	private static final Logger logger = LogManager.getLogger(InsuranceServiceTest.class);
    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private InsuranceService insuranceService;

    private Insurance insurance;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up InsuranceServiceTest");
        MockitoAnnotations.openMocks(this);
        insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("Liberty Seguros");
        insurance.setCoverage("Cobertura completa");
        insurance.setMonthlyPrice(49.99);
        insurance.setCar(Collections.emptyList());
        logger.info("Mocks initialized");
    }

    @Test
    void getAllInsurances_returnsList() {
		logger.info("Testing getAllInsurances method");
		
        when(insuranceRepository.findAll()).thenReturn(List.of(insurance));

        List<Insurance> result = insuranceService.getAllInsurances();

        assertEquals(1, result.size());
        assertEquals("Liberty Seguros", result.get(0).getProvider());
        verify(insuranceRepository).findAll();
        logger.info("Insurances retrieved successfully: {}", result);
    }

    @Test
    void saveInsurance_savesSuccessfully() {
		logger.info("Testing saveInsurance method");
		
        insuranceService.saveInsurance(insurance);

        verify(insuranceRepository).save(insurance);
        logger.info("Insurance saved successfully: {}", insurance);
    }

    @Test
    void getInsuranceById_found() {
    	logger.info("Testing getInsuranceById method");
        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));

        Insurance result = insuranceService.getInsuranceById(1L);

        assertNotNull(result);
        assertEquals("Cobertura completa", result.getCoverage());
        verify(insuranceRepository).findById(1L);
        logger.info("Insurance found: {}", result);
    }

    @Test
    void getInsuranceById_notFound_returnsNull() {
    	logger.info("Testing getInsuranceById method for a non-existing insurance");
        when(insuranceRepository.findById(1L)).thenReturn(Optional.empty());

        Insurance result = insuranceService.getInsuranceById(1L);

        assertNull(result);
        verify(insuranceRepository).findById(1L);
        logger.info("Insurance not found for ID: 1");
    }

    @Test
    void deleteInsurance_callsRepositoryDelete() {
		logger.info("Testing deleteInsurance method");
		
        doNothing().when(insuranceRepository).deleteById(1L);

        insuranceService.deleteInsurance(1L);

        verify(insuranceRepository).deleteById(1L);
        logger.info("Insurance deleted successfully with ID: 1");
    }
}