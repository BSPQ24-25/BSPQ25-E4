package com.carrental.controller;

import com.carrental.dto.CarDTO;
import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    @Autowired
    private CarService carService;

    // Get all cars (for table load)
    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars()
                .stream()
                .map(car -> mapToDTO(car))
                .toList();
    }

    // Search cars (for search feature)
    @GetMapping("/search")
    public List<CarDTO> searchCarsByField(@RequestParam String field, @RequestParam String value) {
        return carService.searchByField(field, value)
                .stream()
                .map(car -> mapToDTO(car))
                .toList();
    }

    // Utility method: Car ➔ CarDTO
    private CarDTO mapToDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setColor(car.getColor());
        dto.setFuelLevel(car.getFuelLevel());
        dto.setTransmission(car.getTransmission());
        dto.setStatus(car.getStatus());
        dto.setMileage(car.getMileage());
        dto.setManufacturingYear(car.getManufacturingYear());
        dto.setInsuranceId(car.getInsurance() != null ? car.getInsurance().getInsuranceId() : null);
        return dto;
    }
}
