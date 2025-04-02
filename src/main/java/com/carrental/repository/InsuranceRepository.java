package com.carrental.repository;

import com.carrental.models.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    // Puedes agregar métodos custom aquí si los necesitas
}
