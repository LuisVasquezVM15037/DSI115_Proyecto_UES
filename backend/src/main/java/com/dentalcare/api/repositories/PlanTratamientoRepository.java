package com.dentalcare.api.repositories;

import com.dentalcare.api.models.PlanTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanTratamientoRepository extends JpaRepository<PlanTratamiento, Integer> {

    // Obtiene todos los planes de tratamiento de una evaluacion clinica especifica
    // Usado para cargar el historial de hallazgos registrados en el odontograma
    List<PlanTratamiento> findByEvaluacionClinica_IdEvaluacionClinica(Integer idEvaluacion);
}
