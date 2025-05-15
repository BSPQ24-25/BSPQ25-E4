package com.carrental.controller;

import com.carrental.models.Car;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addCar(@RequestBody Car car, @RequestParam(required = false) Boolean admin) {
        if (admin == null) {
            return ResponseEntity.badRequest().body("Missing 'admin' parameter.");
        }
        if (!admin) {
            return ResponseEntity.status(500).body("Only admins can add cars.");
        }
        return ResponseEntity.ok(carService.addCar(car));
    }

    @GetMapping
    @ResponseBody
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
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
