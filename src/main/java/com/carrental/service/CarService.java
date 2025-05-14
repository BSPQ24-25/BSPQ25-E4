package com.carrental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrental.models.Car;
import com.carrental.repository.CarRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public void deleteCarById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("Car not found with id: " + id);
        }
        carRepository.deleteById(id);
    }

    public long countAvailableCars() {
        return carRepository.countByStatusIgnoreCase("available");
    }

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> searchByField(String field, String value) {
        try {
            switch (field.toLowerCase()) {
                case "brand":
                    return carRepository.findByBrandContainingIgnoreCase(value);
                case "model":
                    return carRepository.findByModelContainingIgnoreCase(value);
                case "color":
                    return carRepository.findByColorContainingIgnoreCase(value);
                case "transmission":
                    return carRepository.findByTransmissionContainingIgnoreCase(value);
                case "status":
                    return carRepository.findByStatusContainingIgnoreCase(value);
                case "fuellevel":
                    return carRepository.findByFuelLevel(Double.parseDouble(value));
                case "mileage":
                    return carRepository.findByMileage(Integer.parseInt(value));
                case "manufacturingyear":
                    return carRepository.findByManufacturingYear(Integer.parseInt(value));
                case "insuranceid":
                    Long insuranceId = Long.parseLong(value);
                    return carRepository.findAll().stream()
                            .filter(car -> car.getInsuranceId() != null &&
                                    car.getInsuranceId().getInsuranceId().equals(insuranceId))
                            .toList();
                default:
                    return List.of();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for field '" + field + "': " + value);
        }
    }
}
