package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // findById(Integer id) ya viene incluido en JpaRepository
    // Se puede extender aqui si se necesita buscar por nombre de rol
}
