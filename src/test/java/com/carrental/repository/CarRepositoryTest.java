package com.carrental.repository;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@DataJpaTest
class CarRepositoryTest {
	private static final Logger logger = LogManager.getLogger(CarRepositoryTest.class);
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Test
    void saveAndFindCarById() {
    	logger.info("Testing saveAndFindCarById method");
        Insurance insurance = new Insurance();
        insurance.setProvider("Provider A");
        insurance.setCoverage("Full Coverage");
        insurance.setMonthlyPrice(50.0);
        insuranceRepository.save(insurance);

        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Red");
        car.setFuelLevel(100.0);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(50000);
        car.setManufacturingYear(2020);
        car.setInsurance(insurance);

        carRepository.save(car);

        Car foundCar = carRepository.findById(car.getId()).orElse(null);

        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getBrand());
        assertEquals("Corolla", foundCar.getModel());
        assertEquals("Red", foundCar.getColor());
        assertEquals(100.0, foundCar.getFuelLevel());
        assertEquals("Automatic", foundCar.getTransmission());
        assertEquals("Available", foundCar.getStatus());
        assertEquals(50000, foundCar.getMileage());
        assertEquals(2020, foundCar.getManufacturingYear());
        assertEquals("Provider A", foundCar.getInsurance().getProvider());
        logger.info("Car found: {}", foundCar.getBrand());
    }

    @Test
    void findByBrandContainingIgnoreCase() {
    	logger.info("Testing findByBrandContainingIgnoreCase method");
        Car car1 = new Car();
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setBrand("Honda");
        car2.setModel("Civic");
        carRepository.save(car2);

        List<Car> cars = carRepository.findByBrandContainingIgnoreCase("toy");

        assertEquals(1, cars.size());
        assertEquals("Toyota", cars.get(0).getBrand());
        logger.info("Car found: {}", cars.get(0).getBrand());
    }

    @Test
    void findByStatusContainingIgnoreCase() {
    	logger.info("Testing findByStatusContainingIgnoreCase method");
        Car car1 = new Car();
        car1.setStatus("Available");
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setStatus("Unavailable");
        carRepository.save(car2);

        List<Car> cars = carRepository.findByStatusContainingIgnoreCase("available");

        assertEquals(1, cars.size());
        assertEquals("Available", cars.get(0).getStatus());
        logger.info("Car found: {}", cars.get(0).getStatus());
    }

    @Test
    void findByFuelLevel() {
    	logger.info("Testing findByFuelLevel method");
        Car car = new Car();
        car.setFuelLevel(80.0);
        carRepository.save(car);

        List<Car> cars = carRepository.findByFuelLevel(80.0);

        assertEquals(1, cars.size());
        assertEquals(80.0, cars.get(0).getFuelLevel());
        logger.info("Car found with fuel level: {}", cars.get(0).getFuelLevel());
    }

    @Test
    void countByStatusIgnoreCase() {
    	logger.info("Testing countByStatusIgnoreCase method");
        Car car1 = new Car();
        car1.setStatus("Available");
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setStatus("Unavailable");
        carRepository.save(car2);

        long count = carRepository.countByStatusIgnoreCase("available");

        assertEquals(1, count);
        logger.info("Count of available cars: {}", count);
    }

    @Test
    void findByInsuranceId() {
    	logger.info("Testing findByInsuranceId method");
        Insurance insurance = new Insurance();
        insurance.setProvider("Provider B");
        insurance.setCoverage("Basic Coverage");
        insurance.setMonthlyPrice(30.0);
        insuranceRepository.save(insurance);

        Car car = new Car();
        car.setBrand("Ford");
        car.setModel("Focus");
        car.setInsurance(insurance);
        carRepository.save(car);

        List<Car> cars = carRepository.findByInsurance_Id(insurance.getInsuranceId());

        assertEquals(1, cars.size());
        assertEquals("Ford", cars.get(0).getBrand());
        logger.info("Car found with insurance ID: {}", cars.get(0).getInsurance().getInsuranceId());
    }
}