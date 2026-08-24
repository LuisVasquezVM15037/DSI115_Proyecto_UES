package com.dentalcare.api.models;

import com.dentalcare.api.models.enums.NombreRol;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NombreRol nombreRol;
}