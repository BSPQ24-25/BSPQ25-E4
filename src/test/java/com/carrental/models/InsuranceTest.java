package com.carrental.models;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
class InsuranceTest {
	private static final Logger logger = LogManager.getLogger(InsuranceTest.class);
    @Test
    void testInsuranceSettersAndGetters() {
    	logger.info("Testing Insurance getters and setters");
        Insurance insurance = new Insurance();

        insurance.setInsuranceId(1L);
        insurance.setProvider("XYZ Insurance");
        insurance.setCoverage("Full Coverage");
        insurance.setMonthlyPrice(49.99);
        List<Car> cars = new ArrayList<>();
        insurance.setCar(cars);

        assertEquals(1L, insurance.getInsuranceId());
        assertEquals("XYZ Insurance", insurance.getProvider());
        assertEquals("Full Coverage", insurance.getCoverage());
        assertEquals(49.99, insurance.getMonthlyPrice());
        assertNotNull(insurance.getCar());
        assertEquals(0, insurance.getCar().size());
        logger.info("Insurance getters and setters tested successfully");
    }

    @Test
    void testInsuranceFormattedPrice() {
    	logger.info("Testing Insurance formatted price");
        Insurance insurance = new Insurance();
        insurance.setMonthlyPrice(49.99);

        String formattedPrice = insurance.getFormattedPrice();

        assertEquals("49,99€", formattedPrice);
        logger.info("Insurance formatted price tested successfully");
    }

    @Test
    void testInsuranceCarsRelationship() {
    	logger.info("Testing Insurance cars relationship");
        Insurance insurance = new Insurance();
        Car car1 = new Car();
        Car car2 = new Car();
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        insurance.setCar(cars);

        List<Car> insuranceCars = insurance.getCar();

        assertNotNull(insuranceCars);
        assertEquals(2, insuranceCars.size());
        assertTrue(insuranceCars.contains(car1));
        assertTrue(insuranceCars.contains(car2));
        logger.info("Insurance cars relationship tested successfully");
    }

    @Test
    void testInsuranceSetterAndGetter() {
    	logger.info("Testing Insurance setter and getter");
        Insurance insurance = new Insurance();

        insurance.setProvider("ABC Insurance");
        insurance.setCoverage("Comprehensive");
        insurance.setMonthlyPrice(60.50);

        assertEquals("ABC Insurance", insurance.getProvider());
        assertEquals("Comprehensive", insurance.getCoverage());
        assertEquals(60.50, insurance.getMonthlyPrice());
        logger.info("Insurance setter and getter tested successfully");
    }
}