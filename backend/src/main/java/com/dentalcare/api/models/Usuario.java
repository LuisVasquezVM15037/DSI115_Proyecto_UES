package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, unique = true)
    private String usernameUsuario;

    @Column(nullable = false)
    private String passworUsuario; // Se recomienda guardar hasheada (ej. BCrypt)

    @Column(nullable = false, unique = true)
    private String emailUsuario;

    @Column(nullable = false)
    private Boolean esActivo;

    @Column(nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String apellidoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}