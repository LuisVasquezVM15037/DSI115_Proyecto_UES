package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer> {
    // findAll() heredado de JpaRepository es suficiente para poblar el select del frontend
}
