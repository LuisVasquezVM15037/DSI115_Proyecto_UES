package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Prescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescripcionRepository extends JpaRepository<Prescripcion, Integer> {

    // Busca la prescripcion asociada a una cita especifica
    // Una cita puede tener como maximo una prescripcion segun el modelo
    Optional<Prescripcion> findByCita_IdCitas(Integer idCita);
}
