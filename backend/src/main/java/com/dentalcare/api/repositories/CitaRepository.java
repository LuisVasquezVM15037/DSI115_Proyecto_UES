package com.dentalcare.api.repositories;

import com.dentalcare.api.models.Cita;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//La anotación Repository indica que esta interfaz es un repositorio de Spring Data JPA, 
// lo que permite que Spring gestione la implementación de esta interfaz y proporcione funcionalidades de acceso a datos para la entidad Cita.
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    //Los metodos que ya vienen incluidos en JpaRepository son:
    // - save(S entity): Guarda una entidad en la base de datos.
    // - delete(S entity): Elimina una entidad de la base de datos.
    // - findById(ID id): Busca una entidad por su ID.
    // - findAll(): Devuelve una lista de todas las entidades.
    // - count(): Devuelve el número de entidades en la base de datos.
    // - existsById(ID id): Verifica si una entidad con el ID dado existe en la base de datos.
    // - deleteById(ID id): Elimina una entidad por su ID.
    // - deleteAll(): Elimina todas las entidades de la base de datos.
    // - findAllById(Iterable<ID> ids): Devuelve una lista de entidades por sus IDs.
    // - saveAll(Iterable<S> entities): Guarda una lista de entidades en la base de datos.
    // - deleteAllById(Iterable<ID> ids): Elimina una lista de entidades por sus IDs.
    // - deleteAll(Iterable<? extends T> entities): Elimina una lista de entidades.
    // Entre otros
    
    // Además de estos métodos predefinidos, se pueden definir métodos personalizados 
    // utilizando la convención de nomenclatura de Spring Data JPA,
    // como el método findAllByOrderByFechaCitaAscHoraInicioCitaAsc() que se muestra a continuación.

    List<Cita> findAllByOrderByFechaCitaAscHoraInicioCitaAsc();
    //este metodo se crea uniendo el nombre del metodo con la estructura de la consulta que se desea realizar,
    //en este caso se ordena por fecha de cita y hora de inicio de cita de forma ascendente.
    //Aunque el metodo no esta creado con una consulta SQL, Spring Data JPA lo interpreta y genera la consulta correspondiente para obtener los resultados deseados.
}