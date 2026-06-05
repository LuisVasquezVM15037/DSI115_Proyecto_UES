package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    @Column(nullable = false)
    private String nombrePaciente;

    @Column(nullable = false)
    private String apellidoPaciente;

    private String telefonoPaciente;

    @Column(nullable = false)
    private LocalDate fechaNacimientoPaciente;

    @Column(nullable = false, unique = true)
    private String numeroIdentidadPaciente;

    private String emailPaciente;
    private String contactoEmergencia;

        // Alergias conocidas: medicamentos, anestesia, materiales dentales, etc.
    // columnDefinition TEXT permite almacenar descripciones largas
    @Column(columnDefinition = "TEXT")
    private String alergias;
}