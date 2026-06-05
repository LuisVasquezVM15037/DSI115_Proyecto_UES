package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    // Busqueda por numero de identidad para validar duplicados al crear/editar
    Optional<Paciente> findByNumeroIdentidadPaciente(String numeroIdentidadPaciente);

    /**
     * Busqueda combinada por nombre, apellido o numero de identidad.
     * El LOWER y LIKE permiten busqueda case-insensitive con termino parcial.
     * Usado por el buscador del frontend.
     */
    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.nombrePaciente) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(p.apellidoPaciente) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "p.numeroIdentidadPaciente LIKE CONCAT('%', :term, '%')")
    List<Paciente> buscarPorTermino(@Param("term") String term);
}