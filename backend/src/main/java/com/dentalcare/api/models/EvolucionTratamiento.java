package com.dentalcare.api.models;

import com.dentalcare.api.models.enums.EstadoEvolucion;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "evolucion_tratamiento")
public class EvolucionTratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvolucionTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan_tratamiento", nullable = false)
    private PlanTratamiento planTratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEvolucion estadoEvolucionTratamiento;

    @Column(nullable = false)
    private LocalDateTime fechaTratamiento;

    @Column(columnDefinition = "TEXT")
    private String notasEvolucionTratamiento;
}