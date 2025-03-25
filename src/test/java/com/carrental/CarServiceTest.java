package com.carrental;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCar() {
        Car car = new Car();
        car.setBrand("Toyota");
        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carService.addCar(car);
        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getBrand());
    }

    @Test
    void testAddCarWithAllAttributes() {
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Blue");
        car.setFuelLevel(80.5);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(25000);
        car.setManufacturingYear(2020);

        // Create an Insurance object (optional if relationship testing)
        Insurance insurance = new Insurance();
        insurance.setInsuranceId(1L);
        insurance.setProvider("SafeDrive");
        insurance.setCoverage("Full coverage");
        insurance.setMonthlyPrice(50.0);
        car.setInsurance(insurance);

        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carService.addCar(car);

        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getBrand());
        assertEquals("Corolla", savedCar.getModel());
        assertEquals("Blue", savedCar.getColor());
        assertEquals(80.5, savedCar.getFuelLevel());
        assertEquals("Automatic", savedCar.getTransmission());
        assertEquals("Available", savedCar.getStatus());
        assertEquals(25000, savedCar.getMileage());
        assertEquals(2020, savedCar.getManufacturingYear());
        assertEquals("SafeDrive", savedCar.getInsurance().getProvider());
    }

    
    @Test
    void testGetAllCars() {
        Car car1 = new Car();
        car1.setBrand("Toyota");

        Car car2 = new Car();
        car2.setBrand("Ford");

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carService.getAllCars();
        assertEquals(2, cars.size());
        assertEquals("Toyota", cars.get(0).getBrand());
    }

    @Test
    void testGetCarById() {
        Car car = new Car();
        car.setVehicleId(1L);
        car.setBrand("BMW");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(1L);
        assertNotNull(result);
        assertEquals("BMW", result.getBrand());
    }

    @Test
    void testDeleteCar() {
        Long carId = 1L;
        carService.deleteCar(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }
}