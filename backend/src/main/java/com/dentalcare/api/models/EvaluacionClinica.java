package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "evaluacion_clinica")
public class EvaluacionClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacionClinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}