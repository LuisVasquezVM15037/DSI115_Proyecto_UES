package com.dentalcare.api.repositories;

import com.dentalcare.api.models.DetallePrescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePrescripcionRepository extends JpaRepository<DetallePrescripcion, Integer> {

    // Obtiene todos los detalles (medicamentos) de una prescripcion especifica
    List<DetallePrescripcion> findByPrescripcion_IdPrescripcion(Integer idPrescripcion);
}
