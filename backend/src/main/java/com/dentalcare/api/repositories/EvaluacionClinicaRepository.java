package com.dentalcare.api.repositories;

import com.dentalcare.api.models.EvaluacionClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluacionClinicaRepository extends JpaRepository<EvaluacionClinica, Integer> {

    // Busca la evaluacion clinica asociada a una cita especifica
    // Util para saber si ya existe una evaluacion antes de crear una nueva
    Optional<EvaluacionClinica> findByCita_IdCitas(Integer idCita);
}
