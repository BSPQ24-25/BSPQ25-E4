package com.carrental.service;

import com.carrental.models.Car;
import com.carrental.models.Insurance;
import com.carrental.repository.CarRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Insurance insurance = new Insurance();
        insurance.setInsuranceId(1L);

        car = new Car();
        car.setId(1L);
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Red");
        car.setFuelLevel(75.0);
        car.setTransmission("Automatic");
        car.setStatus("available");
        car.setMileage(12000);
        car.setManufacturingYear(2020);
        car.setInsurance(insurance);
    }

    @Test
    void addCar_shouldSaveCar() {
        when(carRepository.save(car)).thenReturn(car);

        Car result = carService.addCar(car);

        assertEquals(car, result);
        verify(carRepository).save(car);
    }

    @Test
    void getAllCars_shouldReturnList() {
        when(carRepository.findAll()).thenReturn(List.of(car));

        List<Car> result = carService.getAllCars();

        assertEquals(1, result.size());
        verify(carRepository).findAll();
    }

    @Test
    void getCarById_found() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(1L);

        assertEquals(car, result);
    }

    @Test
    void getCarById_notFound() {
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        Car result = carService.getCarById(2L);

        assertNull(result);
    }

    @Test
    void deleteCarById_shouldDelete() {
        carService.deleteCarById(1L);
        verify(carRepository).deleteById(1L);
    }

    @Test
    void countAvailableCars_shouldReturnCount() {
        when(carRepository.countByStatusIgnoreCase("available")).thenReturn(3L);

        long count = carService.countAvailableCars();

        assertEquals(3L, count);
    }

    @Test
    void saveCar_shouldSave() {
        carService.saveCar(car);
        verify(carRepository).save(car);
    }

    @Test
    void searchByField_shouldHandleBrand() {
        when(carRepository.findByBrandContainingIgnoreCase("Toyota")).thenReturn(List.of(car));

        List<Car> result = carService.searchByField("brand", "Toyota");

        assertEquals(1, result.size());
        verify(carRepository).findByBrandContainingIgnoreCase("Toyota");
    }

    @Test
    void searchByField_shouldHandleNumericFields() {
        when(carRepository.findByMileage(12000)).thenReturn(List.of(car));

        List<Car> result = carService.searchByField("mileage", "12000");

        assertEquals(1, result.size());
        verify(carRepository).findByMileage(12000);
    }

    @Test
    void searchByField_invalidField_returnsEmptyList() {
        List<Car> result = carService.searchByField("unknown", "value");

        assertTrue(result.isEmpty());
    }
}