package com.dentalcare.api.models;

import com.dentalcare.api.models.enums.EstadoPlan;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plan_tratamiento")
public class PlanTratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlanTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tratamiento", nullable = false)
    private Tratamiento tratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluacion_clinica", nullable = false)
    private EvaluacionClinica evaluacionClinica;

    @Column(nullable = false)
    private Integer piezaDental;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPlan estadoPlan;
}