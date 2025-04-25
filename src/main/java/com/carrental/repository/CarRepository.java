package com.carrental.repository;

import com.carrental.models.Car;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    long countByStatusIgnoreCase(String status);
    List<Car> findByBrandContainingIgnoreCase(String brand);
    List<Car> findByModelContainingIgnoreCase(String model);
    List<Car> findByColorContainingIgnoreCase(String color);
    List<Car> findByTransmissionContainingIgnoreCase(String transmission);
    List<Car> findByStatusContainingIgnoreCase(String status);
    List<Car> findByFuelLevel(double fuelLevel);
    List<Car> findByMileage(int mileage);
    List<Car> findByManufacturingYear(int year);
    List<Car> findByInsurance_Id(Long insuranceId);
    
}
