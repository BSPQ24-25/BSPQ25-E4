package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.carrental.DTO.CarDTO;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public Car addCar(@RequestBody Car car, @RequestParam boolean admin) {
        if (!admin) {
            throw new RuntimeException("Only admins can add cars.");
        }
        return carService.addCar(car);
    }

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars()
                .stream()
                .map(car -> {
                    CarDTO dto = new CarDTO();
                    dto.setId(car.getVehicleId());
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
                })
                .toList();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }
    
    @GetMapping("/search")
    public List<CarDTO> searchCarsByField(@RequestParam String field, @RequestParam String value) {
        return carService.searchByField(field, value)
                .stream()
                .map(car -> {
                    CarDTO dto = new CarDTO();
                    dto.setId(car.getVehicleId());
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
                })
                .toList();
    }



    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id, @RequestParam boolean admin) {
        if (!admin) {
            throw new RuntimeException("Only admins can delete cars.");
        }
        carService.deleteCarById(id);
    }
    
    @GetMapping("/admin/vehicles")
    public String showVehicles(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "admin/edit-vehicle";

    }
}
