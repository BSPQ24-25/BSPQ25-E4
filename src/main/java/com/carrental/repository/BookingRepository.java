package com.carrental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrental.models.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    
    List<Booking> findByBookingStatus(String bookingStatus);

    
    List<Booking> findByBookingStatusIn(List<String> bookingStatuses);

    List<Booking> findByUserNameAndBookingStatusIn(String name, List<String> statuses);

}