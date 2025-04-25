package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // <- CAMBIADO AQUÍ
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    @ResponseBody
    public Car addCar(@RequestBody Car car, @RequestParam boolean admin) {
        if (!admin) {
            throw new RuntimeException("Only admins can add cars.");
        }
        return carService.addCar(car);
    }

    @GetMapping
    @ResponseBody
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
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

    @GetMapping("/user/vehicles")
    public String browseVehicles(Model model) {
        model.addAttribute("vehicles", carService.getAllCars());
        return "user/browse-vehicles";
    }
}
