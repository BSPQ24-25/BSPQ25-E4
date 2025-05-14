package com.carrental.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.NumberFormat;
import java.util.Locale;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


@JsonIgnoreProperties({"cars"})
@Entity
@Table(name = "insurances")
public class Insurance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

    @NotBlank(message = "Provider is required")
    private String provider;

    @NotBlank(message = "Coverage is required")
    private String coverage;

    @Positive(message = "Monthly price must be positive")
    private double monthlyPrice;

    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL)
    private List<Car> cars;

    public Long getInsuranceId() {
        return id;
    }

    public void setInsuranceId(Long insuranceId) {
        this.id = insuranceId;
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

    public String getFormattedPrice() {
        return String.format("%.2f€", this.monthlyPrice);
    }
}
