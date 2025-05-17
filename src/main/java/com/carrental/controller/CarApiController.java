package com.carrental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carrental.dto.CarDTO;
import com.carrental.models.Car;
import com.carrental.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        try {
            List<CarDTO> cars = carService.getAllCars()
                    .stream()
                    .map(this::mapToDTO)
                    .toList();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addCar(@RequestBody Car car, @RequestParam(defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(403).body("Only admins can add cars.");
        }
        return ResponseEntity.ok(carService.addCar(car));
    }

    @GetMapping("/search")
    public List<CarDTO> searchCarsByFields(
            @RequestParam String field,
            @RequestParam String value,
            @RequestParam(required = false) String field2,
            @RequestParam(required = false) String value2) {

        List<Car> cars = carService.searchByField(field, value);

        if (field2 != null && !field2.isEmpty() && value2 != null && !value2.isEmpty()) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean admin) {
        if (!admin) {
            return ResponseEntity.status(403).build();
        }
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }

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
        dto.setInsuranceId(car.getInsuranceId() != null ? car.getInsuranceId().getInsuranceId() : null);
        return dto;
    }
}
