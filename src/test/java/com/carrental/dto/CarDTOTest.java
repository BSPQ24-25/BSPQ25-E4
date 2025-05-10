package com.carrental.dto;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class CarDTOTest {
	private static final Logger logger = LogManager.getLogger(CarDTOTest.class);
    @Test
    void testCarDTOSettersAndGetters() {
    	        logger.info("Starting testCarDTOSettersAndGetters...");
        CarDTO carDTO = new CarDTO();
        
        carDTO.setId(1L);
        carDTO.setBrand("Toyota");
        carDTO.setModel("Corolla");
        carDTO.setColor("Red");
        carDTO.setFuelLevel(75.5);
        carDTO.setTransmission("Automatic");
        carDTO.setStatus("Available");
        carDTO.setMileage(15000);
        carDTO.setManufacturingYear(2020);
        carDTO.setInsuranceId(101L);


        assertEquals(1L, carDTO.getId());
        assertEquals("Toyota", carDTO.getBrand());
        assertEquals("Corolla", carDTO.getModel());
        assertEquals("Red", carDTO.getColor());
        assertEquals(75.5, carDTO.getFuelLevel());
        assertEquals("Automatic", carDTO.getTransmission());
        assertEquals("Available", carDTO.getStatus());
        assertEquals(15000, carDTO.getMileage());
        assertEquals(2020, carDTO.getManufacturingYear());
        assertEquals(101L, carDTO.getInsuranceId());
        logger.info("CarDTO properties set and verified correctly.");
    }

    @Test
    void testCarDTOWithDefaultValues() {
    	        logger.info("Starting testCarDTOWithDefaultValues...");
        CarDTO carDTO = new CarDTO();

        assertNull(carDTO.getId());
        assertNull(carDTO.getBrand());
        assertNull(carDTO.getModel());
        assertNull(carDTO.getColor());
        assertEquals(0.0, carDTO.getFuelLevel());
        assertNull(carDTO.getTransmission());
        assertNull(carDTO.getStatus());
        assertEquals(0, carDTO.getMileage());
        assertEquals(0, carDTO.getManufacturingYear());
        assertNull(carDTO.getInsuranceId());
        logger.info("CarDTO default values verified correctly.");
    }
}