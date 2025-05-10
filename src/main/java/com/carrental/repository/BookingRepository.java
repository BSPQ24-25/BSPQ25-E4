package com.carrental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carrental.models.Booking;
import com.carrental.models.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookingStatus(String bookingStatus);

    List<Booking> findByBookingStatusIn(List<String> bookingStatuses);

    List<Booking> findByUserNameAndBookingStatusIn(String name, List<String> statuses);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = :status")
    long countByBookingStatus(String status);

    @Query("SELECT SUM(b.dailyPrice) FROM Booking b WHERE b.bookingStatus = :status")
    Double sumDailyPriceByBookingStatus(String status);

    @Query("SELECT b FROM Booking b JOIN b.user u WHERE u.name = :name AND b.bookingStatus IN :statuses")
    List<Booking> findByUser_NameAndBookingStatusIn(@Param("name") String name, @Param("statuses") List<String> statuses);
    
    List<Booking> findByUser(User user);
}
