package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_prescripcion")
public class DetallePrescripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetallePrescripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prescripcion", nullable = false)
    private Prescripcion prescripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    @Column(nullable = false)
    private String dosisPrescripcion;

    @Column(nullable = false)
    private String frecuenciaPrescripcion;

    @Column(nullable = false)
    private Integer duracionPrescripcion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String indicacionesPrescripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan_tratamiento")
    private PlanTratamiento planTratamiento;
}