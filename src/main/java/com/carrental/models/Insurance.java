package com.carrental.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "insurances")
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    private String provider;
    private String coverage;
    private double monthlyPrice;

    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL)
    private List<Car> cars;

    // Getters and Setters
    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public List<Car> getCar() {
        return cars;
    }

    public void setCar(List<Car> cars) {
        this.cars = cars;
    }
}
