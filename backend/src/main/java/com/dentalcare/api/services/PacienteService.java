package com.dentalcare.api.services;

import com.dentalcare.api.dtos.Paciente.PacienteRequestDto;
import com.dentalcare.api.dtos.Paciente.PacienteResponseDto;
import com.dentalcare.api.models.Paciente;
import com.dentalcare.api.repositories.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que encapsula toda la logica de negocio para la gestion de pacientes.
 * El controlador solo delega aqui, sin logica propia.
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Inyeccion por constructor: practica recomendada sobre @Autowired en campos
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    // -------------------------------------------------------------------------
    // LISTAR todos los pacientes
    // -------------------------------------------------------------------------
    public List<PacienteResponseDto> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // BUSCAR pacientes por termino (nombre, apellido o DUI)
    // -------------------------------------------------------------------------
    public List<PacienteResponseDto> buscar(String term) {
        return pacienteRepository.buscarPorTermino(term)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // CREAR paciente
    // -------------------------------------------------------------------------
    public PacienteResponseDto crearPaciente(PacienteRequestDto request) {

        // Validamos que el numero de identidad no este ya registrado
        if (pacienteRepository.findByNumeroIdentidadPaciente(request.getNumeroIdentidadPaciente()).isPresent()) {
            throw new RuntimeException("El numero de identidad '" + request.getNumeroIdentidadPaciente() + "' ya esta registrado.");
        }

        // Construimos la entidad a partir del DTO
        Paciente paciente = new Paciente();
        mapRequestToEntity(request, paciente);

        return toResponseDto(pacienteRepository.save(paciente));
    }

    // -------------------------------------------------------------------------
    // EDITAR paciente
    // -------------------------------------------------------------------------
    public PacienteResponseDto actualizarPaciente(Integer id, PacienteRequestDto request) {

        // Verificamos que el paciente a editar exista
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente con id " + id + " no encontrado."));

        // Validamos unicidad de identidad solo si cambio respecto al valor actual
        if (!paciente.getNumeroIdentidadPaciente().equals(request.getNumeroIdentidadPaciente()) &&
             pacienteRepository.findByNumeroIdentidadPaciente(request.getNumeroIdentidadPaciente()).isPresent()) {
            throw new RuntimeException("El numero de identidad '" + request.getNumeroIdentidadPaciente() + "' ya esta registrado.");
        }

        // Actualizamos los campos de la entidad con los valores del DTO
        mapRequestToEntity(request, paciente);

        return toResponseDto(pacienteRepository.save(paciente));
    }

    // -------------------------------------------------------------------------
    // ELIMINAR paciente
    // -------------------------------------------------------------------------
    public void eliminarPaciente(Integer id) {
        if (!pacienteRepository.existsById(id))
            throw new RuntimeException("Paciente con id " + id + " no encontrado.");
        pacienteRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Metodo privado: mapea los campos del DTO a la entidad (reutilizado en crear y editar)
    // -------------------------------------------------------------------------
    private void mapRequestToEntity(PacienteRequestDto request, Paciente paciente) {
        paciente.setNombrePaciente(request.getNombrePaciente());
        paciente.setApellidoPaciente(request.getApellidoPaciente());
        paciente.setTelefonoPaciente(request.getTelefonoPaciente());
        paciente.setFechaNacimientoPaciente(request.getFechaNacimientoPaciente());
        paciente.setNumeroIdentidadPaciente(request.getNumeroIdentidadPaciente());
        paciente.setEmailPaciente(request.getEmailPaciente());
        paciente.setContactoEmergencia(request.getContactoEmergencia());
        paciente.setAlergias(request.getAlergias());
    }

    // -------------------------------------------------------------------------
    // Metodo privado: convierte entidad a DTO de respuesta
    // -------------------------------------------------------------------------
    private PacienteResponseDto toResponseDto(Paciente p) {
        return new PacienteResponseDto(
                p.getIdPaciente(),
                p.getNombrePaciente(),
                p.getApellidoPaciente(),
                p.getTelefonoPaciente(),
                p.getFechaNacimientoPaciente(),
                p.getNumeroIdentidadPaciente(),
                p.getEmailPaciente(),
                p.getContactoEmergencia(),
                p.getAlergias()
        );
    }
}