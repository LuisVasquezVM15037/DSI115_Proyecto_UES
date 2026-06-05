package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {
    // findAll() heredado de JpaRepository es suficiente para poblar el select del frontend
}
