package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
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
