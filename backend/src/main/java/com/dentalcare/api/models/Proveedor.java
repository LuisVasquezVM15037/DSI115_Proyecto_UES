package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;

    @Column(nullable = false)
    private String razonSocial;

    // String para manejar guiones típicos en documentos fiscales
    @Column(nullable = false)
    private String nit;

    @Column(nullable = false)
    private String nrc;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombreContacto;

    @Column(nullable = false)
    private Boolean esActivo;
}