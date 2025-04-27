package com.carrental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.CarDTO;
import com.carrental.models.Car;
import com.carrental.service.CarService;

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

    @GetMapping("/search")
    public List<CarDTO> searchCarsByFields( 
        @RequestParam String field,
        @RequestParam String value,
        @RequestParam(required = false) String field2,
        @RequestParam(required = false) String value2) {
    
    List<Car> cars = carService.searchByField(field, value);

    if (field2 != null && !field2.isEmpty() && value2 != null && !value2.isEmpty()) {
        // Filter using getters
        cars = cars.stream()
                .filter(car -> {
                    try {
                        String methodName = "get" + field2.substring(0, 1).toUpperCase() + field2.substring(1);
                        Object fieldValue = Car.class.getMethod(methodName).invoke(car);
                        return fieldValue != null && fieldValue.toString().equalsIgnoreCase(value2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .toList();
    }

    return cars.stream()
            .map(this::mapToDTO)
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
