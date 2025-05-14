package com.carrental.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.carrental.dto.CarDTO;
import com.carrental.models.Car;
import com.carrental.service.CarService;

/**
 * @brief REST API Controller for handling car-related operations.
 * 
 * This controller provides endpoints to fetch all cars
 * and to search cars based on one or two filter criteria.
 */
@RestController
@RequestMapping("/api/cars")
public class CarApiController {

    /**
     * Service for managing car entities.
     */
    @Autowired
    private CarService carService;

    /**
     * @brief Retrieves all cars.
     * 
     * @return List of all cars mapped to CarDTO objects.
     */
    // Get all cars (for table load)
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

    /**
     * @brief Searches cars based on one or two fields.
     * 
     * @param field The primary field to search by (e.g., brand, model).
     * @param value The value to match in the primary field.
     * @param field2 (Optional) A second field to search by.
     * @param value2 (Optional) The value to match in the second field.
     * @return List of cars matching the given search criteria, mapped to CarDTO.
     */
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
      

    /**
     * @brief Maps a Car entity to a CarDTO object.
     * 
     * @param car The Car entity to map.
     * @return The corresponding CarDTO.
     */
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
        dto.setInsuranceId(car.getInsuranceId() != null ? car.getInsuranceId().getInsuranceId() : null);
        return dto;
    }
    
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCar(@PathVariable Long id, @RequestParam(required = false) Boolean admin) {
        if (admin == null) {
            return ResponseEntity.badRequest().body("Missing 'admin' parameter.");
        }
        if (!admin) {
            return ResponseEntity.status(500).body("Only admins can delete cars.");
        }
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }


    
}
