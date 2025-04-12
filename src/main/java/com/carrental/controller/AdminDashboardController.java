package com.carrental.controller;

import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.repository.CarRepository;
import com.carrental.repository.InsuranceRepository;
import com.carrental.service.BookingService;


@Controller
public class AdminDashboardController {

	@Autowired
	private InsuranceRepository insuranceRepository;

	@Autowired
	private CarRepository carRepository;
	
    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        long availableCars = carService.countAvailableCars();
        long activeRentals = bookingService.countActiveBookings();
        double totalRevenue = bookingService.getTotalRevenue();

        model.addAttribute("availableCars", availableCars);
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin_dashboard";
    }
    
    @GetMapping("/admin/vehicles")
    public String showVehicleList(Model model) {
        model.addAttribute("vehicles", carService.getAllCars());
        return "admin/vehicle-management";
    }
    
    @PostMapping("/admin/vehicles/add")
    public String addVehicle(@ModelAttribute Car car, @RequestParam(name = "insuranceId", required = false) Long insuranceId) {
        if (insuranceId != null) {
            Insurance insurance = insuranceRepository.findById(insuranceId).orElse(null);
            car.setInsurance(insurance);
        }
        carRepository.save(car);
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/admin/vehicles/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        carService.deleteCarById(id);
        return "redirect:/admin/vehicles";
    }
    
    @GetMapping("/admin/vehicles/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        model.addAttribute("insurances", insuranceRepository.findAll()); // Por si necesitas mostrar opciones de seguro
        return "admin/edit-vehicle";
    }
    
    @PostMapping("/admin/vehicles/edit")
    public String updateVehicle(@ModelAttribute Car car, @RequestParam(name = "insuranceId", required = false) Long insuranceId) {
        if (insuranceId != null) {
            Insurance insurance = insuranceRepository.findById(insuranceId).orElse(null);
            car.setInsurance(insurance);
        }

        carService.saveCar(car);
        return "redirect:/admin/vehicles";
    }



}


