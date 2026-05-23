package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "medicamento")
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false)
    private String nombreMedicamento;

    @Column(nullable = false)
    private String componenteActivo;

    @Column(nullable = false)
    private String concentracion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoMedicamento;

    @Column(nullable = false)
    private Integer cantidadInventario;
}