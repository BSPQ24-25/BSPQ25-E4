package com.carrental.DTO;

public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private String color;
    private double fuelLevel;
    private String transmission;
    private String status;
    private int mileage;
    private int manufacturingYear;
    private Long insuranceId; // Just the ID, not full insurance

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public double getFuelLevel() {
        return fuelLevel;
    }
    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
    public String getTransmission() {
        return transmission;
    }
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getMileage() {
        return mileage;
    }
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
    public int getManufacturingYear() {
        return manufacturingYear;
    }
    public void setManufacturingYear(int manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }
    public Long getInsuranceId() {
        return insuranceId;
    }
    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }
}
