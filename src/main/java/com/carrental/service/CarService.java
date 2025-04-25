package com.carrental.service;

import com.carrental.models.Car;
import com.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }
    
    public long countAvailableCars() {
        return carRepository.countByStatusIgnoreCase("available");
    }
    
    public void saveCar(Car car) {
        carRepository.save(car);
    }
    
    public List<Car> searchByField(String field, String value) {
        switch (field.toLowerCase()) {
            case "brand": return carRepository.findByBrandContainingIgnoreCase(value);
            case "model": return carRepository.findByModelContainingIgnoreCase(value);
            case "color": return carRepository.findByColorContainingIgnoreCase(value);
            case "transmission": return carRepository.findByTransmissionContainingIgnoreCase(value);
            case "status": return carRepository.findByStatusContainingIgnoreCase(value);
            case "fuellevel": return carRepository.findByFuelLevel(Double.parseDouble(value));
            case "mileage": return carRepository.findByMileage(Integer.parseInt(value));
            case "manufacturingyear": return carRepository.findByManufacturingYear(Integer.parseInt(value));
            case "insuranceid": return carRepository.findByInsurance_Id(Long.parseLong(value));
            default: return List.of(); // Lista vacía si no se reconoce el campo
        }
    }
}
