package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tratamiento")
public class Tratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTratamiento;

    @Column(nullable = false)
    private String nombreTratamiento;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcionTratamiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTratamiento;
}