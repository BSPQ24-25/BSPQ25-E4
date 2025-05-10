package com.carrental.repository;

import com.carrental.models.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@DataJpaTest
class InsuranceRepositoryTest {
	private static final Logger logger = LogManager.getLogger(InsuranceRepositoryTest.class);
    @Autowired
    private InsuranceRepository insuranceRepository;

    @Test
    void saveAndFindInsuranceById() {
    	logger.info("Testing saveAndFindInsuranceById method");
        Insurance insurance = new Insurance();
        insurance.setProvider("Provider A");
        insurance.setCoverage("Full Coverage");
        insurance.setMonthlyPrice(50.0);
        insuranceRepository.save(insurance);

        Insurance foundInsurance = insuranceRepository.findById(insurance.getInsuranceId()).orElse(null);

        assertNotNull(foundInsurance);
        assertEquals("Provider A", foundInsurance.getProvider());
        assertEquals("Full Coverage", foundInsurance.getCoverage());
        assertEquals(50.0, foundInsurance.getMonthlyPrice());
        logger.info("Insurance found: {}", foundInsurance.getProvider());
    }

    @Test
    void findInsuranceById_shouldReturnEmptyIfNotFound() {
    	logger.info("Testing findInsuranceById method for non-existing insurance");
        Insurance result = insuranceRepository.findById(999L).orElse(null);
        

        assertNull(result);
        logger.info("No insurance found with the given ID");
    }

    @Test
    void saveInsurance_shouldPersist() {
    	logger.info("Testing saveInsurance method");
        Insurance insurance = new Insurance();
        insurance.setProvider("Provider B");
        insurance.setCoverage("Basic Coverage");
        insurance.setMonthlyPrice(30.0);

        Insurance savedInsurance = insuranceRepository.save(insurance);

        assertNotNull(savedInsurance);
        assertEquals("Provider B", savedInsurance.getProvider());
        assertEquals("Basic Coverage", savedInsurance.getCoverage());
        assertEquals(30.0, savedInsurance.getMonthlyPrice());
        logger.info("Insurance saved successfully: {}", savedInsurance.getProvider());
    }
}