package com.carrental.repository;

import com.carrental.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    long countByStatusIgnoreCase(String status);
}
