package com.carrental.repository;

import com.carrental.models.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InsuranceRepositoryTest {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Test
    void saveAndFindInsuranceById() {
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
    }

    @Test
    void findInsuranceById_shouldReturnEmptyIfNotFound() {
        Insurance result = insuranceRepository.findById(999L).orElse(null);

        assertNull(result);
    }

    @Test
    void saveInsurance_shouldPersist() {
        Insurance insurance = new Insurance();
        insurance.setProvider("Provider B");
        insurance.setCoverage("Basic Coverage");
        insurance.setMonthlyPrice(30.0);

        Insurance savedInsurance = insuranceRepository.save(insurance);

        assertNotNull(savedInsurance);
        assertEquals("Provider B", savedInsurance.getProvider());
        assertEquals("Basic Coverage", savedInsurance.getCoverage());
        assertEquals(30.0, savedInsurance.getMonthlyPrice());
    }
}