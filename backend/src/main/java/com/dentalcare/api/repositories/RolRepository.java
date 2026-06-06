package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Rol;
import com.dentalcare.api.models.enums.NombreRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombreRol(NombreRol nombreRol);
}

