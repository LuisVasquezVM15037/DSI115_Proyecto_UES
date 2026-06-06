//Archivo agregado para el pbi de revisar accesos, se crea el repositorio para manejar las consultas a la bd para el modelo que cree de registroAcceso
package com.dentalcare.api.repositories;

import com.dentalcare.api.models.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Integer> {
}