package com.carrental.service;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class CarServiceTest {
	private static final Logger logger = LogManager.getLogger(CarServiceTest.class);
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
    	logger.info("Setting up CarServiceTest");
        MockitoAnnotations.openMocks(this);
        logger.info("Mocks initialized");
    }

    @Test
    void testAddCar() {
    	logger.info("Testing addCar method");
        Car car = new Car();
        car.setBrand("Toyota");
        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carService.addCar(car);
        assertNotNull(savedCar);
        assertEquals("Toyota", savedCar.getBrand());
        logger.info("Car added successfully: {}", savedCar);
    }

    @Test
    void testAddCarWithAllAttributes() {
    	logger.info("Testing addCarWithAllAttributes method");
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Blue");
        car.setFuelLevel(80.5);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(25000);
        car.setManufacturingYear(2020);

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
        logger.info("Car with all attributes added successfully: {}", savedCar);
    }

    @Test
    void testGetAllCars() {
    	logger.info("Testing getAllCars method");
        Car car1 = new Car();
        car1.setBrand("Toyota");

        Car car2 = new Car();
        car2.setBrand("Ford");

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carService.getAllCars();
        assertEquals(2, cars.size());
        assertEquals("Toyota", cars.get(0).getBrand());
        logger.info("Retrieved all cars successfully: {}", cars);
    }

    @Test
    void testGetCarById() {
    	logger.info("Testing getCarById method");
        Car car = new Car();
        car.setId(1L);
        car.setBrand("BMW");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(1L).orElseThrow();
        assertNotNull(result);
        assertEquals("BMW", result.getBrand());
        logger.info("Retrieved car by ID successfully: {}", result);
    }

    @Test
    void testGetCarById_CarNotFound() {
		logger.info("Testing getCarById_CarNotFound method");
		
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.getCarById(1L).orElseThrow(EntityNotFoundException::new));
        logger.info("Car not found as expected");
    }

    @Test
    void testDeleteCar() {
    	logger.info("Testing deleteCar method");
        Long carId = 1L;
        when(carRepository.existsById(carId)).thenReturn(true);

        carService.deleteCarById(carId);
        verify(carRepository, times(1)).deleteById(carId);
        logger.info("Car deleted successfully with ID: {}", carId);
    }

    @Test
    void testDeleteCar_CarNotFound() {
		logger.info("Testing deleteCar_CarNotFound method");
		
        Long carId = 1L;
        when(carRepository.existsById(carId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> carService.deleteCarById(carId));
        logger.info("Car not found as expected for deletion");
    }
}