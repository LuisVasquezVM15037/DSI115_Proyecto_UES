package com.dentalcare.api.models;

import com.dentalcare.api.models.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cita")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCitas; // Nota: en la BD dice id_citas

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_odontologo", nullable = false)
    private Odontologo odontologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fechaCita;

    @Column(nullable = false)
    private LocalDateTime horaInicioCita;

    @Column(nullable = false)
    private LocalDateTime horaFinCita;

    private String motivoCancelacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estadoCita;
}