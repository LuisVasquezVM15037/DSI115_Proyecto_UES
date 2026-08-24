package com.dentalcare.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "odontologo")
public class Odontologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOdontologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String especialidadOdontologo;

    // String es ideal para la JVPO u otros identificadores que pueden incluir guiones
    @Column(nullable = false, unique = true)
    private String jvpoId; 
}