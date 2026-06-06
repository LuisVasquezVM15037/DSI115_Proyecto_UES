package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Spring Data genera el SQL automaticamente a partir del nombre del metodo
    Optional<Usuario> findByUsernameUsuario(String usernameUsuario);

    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    /**
     * Busca por username O email en una sola consulta.
     * Util para que el login acepte cualquiera de los dos como identificador.
     */
    @Query("SELECT u FROM Usuario u WHERE u.usernameUsuario = :identifier OR u.emailUsuario = :identifier")
    Optional<Usuario> findByUsernameOrEmail(@Param("identifier") String identifier);
    // Con solo esta interfaz, ya tenemos acceso a métodos como CRUD completo,
    // Paginación y ordenamiento, Operaciones batch y flush, Queries dinámicas por ejemplo,
    // Generación automática de consultas por nombre de método

}
