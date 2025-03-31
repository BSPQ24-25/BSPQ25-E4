package com.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrental.models.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // You can add custom queries here later if needed

}