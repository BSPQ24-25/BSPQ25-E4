package com.carrental.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class CarTest {

    @Test
    void testCarSettersAndGetters() {
        Car car = new Car();
        Insurance insurance = new Insurance();
        insurance.setProvider("XYZ Insurance");
        insurance.setCoverage("Full Coverage");
        insurance.setMonthlyPrice(49.99);
        
        List<Booking> bookings = new ArrayList<>();
        
        car.setId(1L);
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Red");
        car.setFuelLevel(75.0);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(50000);
        car.setManufacturingYear(2018);
        car.setInsurance(insurance);
        car.setBookings(bookings);

        assertEquals(1L, car.getId());
        assertEquals("Toyota", car.getBrand());
        assertEquals("Corolla", car.getModel());
        assertEquals("Red", car.getColor());
        assertEquals(75.0, car.getFuelLevel());
        assertEquals("Automatic", car.getTransmission());
        assertEquals("Available", car.getStatus());
        assertEquals(50000, car.getMileage());
        assertEquals(2018, car.getManufacturingYear());
        assertNotNull(car.getInsurance());
        assertEquals("XYZ Insurance", car.getInsurance().getProvider());
        assertNotNull(car.getBookings());
        assertTrue(car.getBookings().isEmpty());
    }

    @Test
    void testCarInsuranceRelationship() {
        Car car = new Car();
        Insurance insurance = new Insurance();
        insurance.setProvider("XYZ Insurance");
        
        car.setInsurance(insurance);
        
        assertNotNull(car.getInsurance());
        assertEquals("XYZ Insurance", car.getInsurance().getProvider());
    }

    @Test
    void testCarBookingsRelationship() {
        Car car = new Car();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking(); 
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        car.setBookings(bookings);
        
        List<Booking> carBookings = car.getBookings();
        assertNotNull(carBookings);
        assertEquals(2, carBookings.size());
        assertTrue(carBookings.contains(booking1));
        assertTrue(carBookings.contains(booking2));
    }

    @Test
    void testCarSetterAndGetter() {
        Car car = new Car();
        
        car.setBrand("Honda");
        car.setModel("Civic");
        car.setColor("Blue");
        car.setFuelLevel(60.0);
        car.setTransmission("Manual");
        car.setStatus("Rented");
        car.setMileage(35000);
        car.setManufacturingYear(2020);
        
        assertEquals("Honda", car.getBrand());
        assertEquals("Civic", car.getModel());
        assertEquals("Blue", car.getColor());
        assertEquals(60.0, car.getFuelLevel());
        assertEquals("Manual", car.getTransmission());
        assertEquals("Rented", car.getStatus());
        assertEquals(35000, car.getMileage());
        assertEquals(2020, car.getManufacturingYear());
    }
}