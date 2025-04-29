package com.carrental.repository;

import com.carrental.models.Booking;
import com.carrental.models.Car;
import com.carrental.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void saveAndFindBookingById() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setColor("Blue");
        car.setFuelLevel(90.0);
        car.setTransmission("Automatic");
        car.setStatus("Available");
        car.setMileage(30000);
        car.setManufacturingYear(2019);
        carRepository.save(car);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setBookingStatus("pending");
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(7));
        booking.setDailyPrice(50.0);
        booking.setPaymentMethod("Credit Card");
        booking.setSecurityDeposit(100.0);
        bookingRepository.save(booking);

        Booking foundBooking = bookingRepository.findById(booking.getBookingId()).orElse(null);

        assertNotNull(foundBooking);
        assertEquals("John Doe", foundBooking.getUser().getName());
        assertEquals("Toyota", foundBooking.getCar().getBrand());
        assertEquals("pending", foundBooking.getBookingStatus());
    }

    @Test
    void findByBookingStatus() {
        Booking booking1 = new Booking();
        booking1.setBookingStatus("confirmed");
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBookingStatus("pending");
        bookingRepository.save(booking2);

        List<Booking> confirmedBookings = bookingRepository.findByBookingStatus("confirmed");

        assertEquals(1, confirmedBookings.size());
        assertEquals("confirmed", confirmedBookings.get(0).getBookingStatus());
    }

    @Test
    void findByBookingStatusIn() {
        Booking booking1 = new Booking();
        booking1.setBookingStatus("confirmed");
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBookingStatus("pending");
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findByBookingStatusIn(List.of("confirmed", "pending"));

        assertEquals(2, bookings.size());
    }

    @Test
    void findByUserNameAndBookingStatusIn() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("securePassword");
        userRepository.save(user);

        Booking booking1 = new Booking();
        booking1.setUser(user);
        booking1.setBookingStatus("confirmed");
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setUser(user);
        booking2.setBookingStatus("pending");
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findByUserNameAndBookingStatusIn("Alice", List.of("confirmed", "pending"));

        assertEquals(2, bookings.size());
    }

    @Test
    void countByBookingStatus() {
        Booking booking1 = new Booking();
        booking1.setBookingStatus("confirmed");
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBookingStatus("confirmed");
        bookingRepository.save(booking2);

        long count = bookingRepository.countByBookingStatus("confirmed");

        assertEquals(2, count);
    }

    @Test
    void sumDailyPriceByBookingStatus() {
        Booking booking1 = new Booking();
        booking1.setBookingStatus("completed");
        booking1.setDailyPrice(100.0);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBookingStatus("completed");
        booking2.setDailyPrice(150.0);
        bookingRepository.save(booking2);

        Double total = bookingRepository.sumDailyPriceByBookingStatus("completed");

        assertNotNull(total);
        assertEquals(250.0, total);
    }

    @Test
    void findByUser() {
        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setPassword("bobPassword");
        userRepository.save(user);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingStatus("confirmed");
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByUser(user);

        assertEquals(1, bookings.size());
        assertEquals("Bob", bookings.get(0).getUser().getName());
    }
}