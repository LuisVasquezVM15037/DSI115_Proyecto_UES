package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Cita;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    //Con solo esta interfaz, ya tenemos acceso a métodos como CRUD completo, Paginación y ordenamiento, 
    // Operaciones batch y flush, Queries dinámicas por ejemplo, Generación automática de consultas por nombre de método

    // Método personalizado para obtener todas las citas ordenadas por fecha e inicio de cita en orden ascendente
    List<Cita> findAllByOrderByFechaCitaAscHoraInicioCitaAsc();
}