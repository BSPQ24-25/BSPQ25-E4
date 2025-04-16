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
}
