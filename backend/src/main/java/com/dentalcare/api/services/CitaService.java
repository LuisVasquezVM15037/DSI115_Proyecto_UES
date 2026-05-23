package com.dentalcare.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dentalcare.api.dtos.Cita.CitaResponseDTO;
import com.dentalcare.api.dtos.Cita.CitaCancelacionDTO;
import com.dentalcare.api.dtos.Cita.CitaRequestDTO;
import com.dentalcare.api.models.Cita;
import com.dentalcare.api.models.Odontologo;
import com.dentalcare.api.models.Paciente;
import com.dentalcare.api.repositories.CitaRepository;
import com.dentalcare.api.repositories.OdontologoRepository;
import com.dentalcare.api.repositories.PacienteRepository;

@Service
public class CitaService {

    // Inyectamos los repositorios necesarios para acceder a la base de datos
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private OdontologoRepository odontologoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // ====================================================
    // Metodos publicos para la logica de negocio de Citas
    // ====================================================

    // este metodo se encarga de obtener todas las citas de la base de datos y
    // convertirlas a DTOs para enviarlas al frontend
    public List<CitaResponseDTO> obtenerTodas() {
        // Obtenemos las entidades de la BD y las transformamos a Response DTOs
        List<Cita> citas = citaRepository.findAllByOrderByFechaCitaAscHoraInicioCitaAsc();
        // Usamos Stream para mapear cada entidad a un DTO de respuesta
        return citas.stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    // este metodo se encarga de crear una nueva cita a partir de los datos que
    // vienen del frontend, validando que el odontologo y paciente existan,
    // y luego guardando la cita en la base de datos
    public CitaResponseDTO crearCita(CitaRequestDTO request) {
        // Validar que las entidades relacionadas existan
        Odontologo odontologo = odontologoRepository.findById(request.getIdOdontologo())
                .orElseThrow(() -> new RuntimeException("Error: Odontólogo no encontrado"));
        // Validar que el paciente exista
        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Error: Paciente no encontrado"));

        // Convertir el Request a Entidad
        Cita nuevaCita = mapearAEntidad(request, odontologo, paciente);

        // Guardar en la base de datos
        Cita citaGuardada = citaRepository.save(nuevaCita);

        // Convertir la Entidad guardada a Response y devolverla
        return mapearAResponse(citaGuardada);
    }

    // ==================================================
    // Metodos Privaos para mapeo entre Entidades y DTOs
    // ==================================================

    /**
     * Convierte los datos que vienen del Frontend (Request) en un objeto
     * que la Base de Datos pueda entender (Entidad).
     */
    private Cita mapearAEntidad(CitaRequestDTO request, Odontologo odontologo, Paciente paciente) {
        // Creamos una nueva instancia de Cita y le asignamos los datos del Request
        Cita cita = new Cita();
        cita.setOdontologo(odontologo);
        cita.setPaciente(paciente);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraInicioCita(request.getHoraInicioCita());
        cita.setHoraFinCita(request.getHoraFinCita());

        // Si el estado de la cita viene nulo desde el frontend, lo forzamos a
        // PROGRAMADA por defecto
        if (request.getEstadoCita() != null) {
            cita.setEstadoCita(request.getEstadoCita());
        } else {
            cita.setEstadoCita(com.dentalcare.api.models.enums.EstadoCita.PROGRAMADA);
        }
        return cita;
    }

    /**
     * Convierte la Entidad de la Base de Datos en un objeto "plano"
     * y seguro para enviar al Frontend (Response).
     */
    private CitaResponseDTO mapearAResponse(Cita cita) {
        // Creamos una nueva instancia de CitaResponseDTO y le asignamos los datos de la
        // Entidad
        CitaResponseDTO response = new CitaResponseDTO();

        // Datos propios de la cita
        response.setIdCitas(cita.getIdCitas());
        response.setFechaCita(cita.getFechaCita());
        response.setHoraInicioCita(cita.getHoraInicioCita());
        response.setHoraFinCita(cita.getHoraFinCita());
        response.setEstadoCita(cita.getEstadoCita());
        response.setMotivoCancelacion(cita.getMotivoCancelacion());

        // los datos del paciente se aplanan para evitar enviar objetos anidados al
        // frontend, lo que puede causar problemas de serialización y seguridad
        if (cita.getPaciente() != null) {
            response.setIdPaciente(cita.getPaciente().getIdPaciente());
            response.setNombreCompletoPaciente(
                    cita.getPaciente().getNombrePaciente() + " " + cita.getPaciente().getApellidoPaciente());
            // Agregamos el número de identidad del paciente al DTO de respuesta para que el
            // frontend pueda mostrarlo sin necesidad de hacer otra consulta
            response.setNumeroIdentidadPaciente(cita.getPaciente().getNumeroIdentidadPaciente());
        }

        // Aplanando los datos del Odontólogo
        if (cita.getOdontologo() != null) {
            response.setIdOdontologo(cita.getOdontologo().getIdOdontologo());
            response.setEspecialidadOdontologo(cita.getOdontologo().getEspecialidadOdontologo());
        }

        return response;
    }

    // Metodo para cancelar una cita, que actualiza el estado de la cita a CANCELADA
    // y guarda el motivo de cancelación en la base de datos.

    public CitaResponseDTO cancelarCita(Integer id, CitaCancelacionDTO cancelacionRequest) {
        // Buscar la cita existente
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cita no encontrada con el ID: " + id));

        // Aplicar los cambios de estado y motivo
        cita.setEstadoCita(com.dentalcare.api.models.enums.EstadoCita.CANCELADA);
        cita.setMotivoCancelacion(cancelacionRequest.getMotivoCancelacion());

        // Guardar los cambios en la BD (JPA hace un update automáticamente al encontrar
        // el ID)
        Cita citaActualizada = citaRepository.save(cita);

        // Mapear a Response y retornar
        return mapearAResponse(citaActualizada);
    }

    // Metodo para actualizar una cita, que permite modificar los datos de la cita
    public CitaResponseDTO actualizarCita(Integer id, CitaRequestDTO request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Odontologo odontologo = odontologoRepository.findById(request.getIdOdontologo())
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        // Actualizar los campos de la cita con los datos del request
        cita.setOdontologo(odontologo);
        cita.setPaciente(paciente);
        cita.setFechaCita(request.getFechaCita());
        cita.setHoraInicioCita(request.getHoraInicioCita());
        cita.setHoraFinCita(request.getHoraFinCita());
        cita.setEstadoCita(request.getEstadoCita());

        Cita actualizada = citaRepository.save(cita);
        return mapearAResponse(actualizada); // El método de mapeo manual que ya tienes
    }
}