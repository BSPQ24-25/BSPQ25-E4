package com.carrental.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrental.models.Insurance;
import com.carrental.repository.InsuranceRepository;

@Service
public class InsuranceService {

    @Autowired
    private InsuranceRepository insuranceRepository;

    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    public void saveInsurance(Insurance insurance) {
        insuranceRepository.save(insurance);
    }

    public Insurance getInsuranceById(Long id) {
        return insuranceRepository.findById(id).orElse(null);
    }

    public void deleteInsurance(Long id) {
        insuranceRepository.deleteById(id);
    }
}
