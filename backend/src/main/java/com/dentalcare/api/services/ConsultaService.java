package com.dentalcare.api.services;

import com.dentalcare.api.dtos.Consulta.Medicamento.MedicamentoResponseDTO;
import com.dentalcare.api.dtos.Consulta.Evaluacion.EvaluacionClinicaRequestDTO;
import com.dentalcare.api.dtos.Consulta.Evaluacion.EvaluacionClinicaResponseDTO;
import com.dentalcare.api.dtos.Consulta.Prescripcion.PrescripcionRequestDTO;
import com.dentalcare.api.dtos.Consulta.Prescripcion.PrescripcionResponseDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.PlanTratamientoRequestDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.PlanTratamientoResponseDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.TratamientoRequestDTO;
import com.dentalcare.api.dtos.Consulta.Tratamiento.TratamientoResponseDTO;
import com.dentalcare.api.models.*;
import com.dentalcare.api.models.enums.EstadoPlan;
import com.dentalcare.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que orquesta el flujo completo de una consulta odontologica.
 * Maneja la secuencia: Evaluacion → Planes de tratamiento → Prescripcion.
 * Usa @Transactional para garantizar que si algo falla, todo se revierte.
 */
@Service
public class ConsultaService {

        private final CitaRepository citaRepository;
        private final EvaluacionClinicaRepository evaluacionRepository;
        private final PlanTratamientoRepository planTratamientoRepository;
        private final TratamientoRepository tratamientoRepository;
        private final PrescripcionRepository prescripcionRepository;
        private final DetallePrescripcionRepository detalleRepository;
        private final MedicamentoRepository medicamentoRepository;

        // Inyeccion por constructor
        public ConsultaService(CitaRepository citaRepository,
                        EvaluacionClinicaRepository evaluacionRepository,
                        PlanTratamientoRepository planTratamientoRepository,
                        TratamientoRepository tratamientoRepository,
                        PrescripcionRepository prescripcionRepository,
                        DetallePrescripcionRepository detalleRepository,
                        MedicamentoRepository medicamentoRepository) {
                this.citaRepository = citaRepository;
                this.evaluacionRepository = evaluacionRepository;
                this.planTratamientoRepository = planTratamientoRepository;
                this.tratamientoRepository = tratamientoRepository;
                this.prescripcionRepository = prescripcionRepository;
                this.detalleRepository = detalleRepository;
                this.medicamentoRepository = medicamentoRepository;
        }

        // =========================================================================
        // PASO 1: EVALUACION CLINICA
        // =========================================================================

        /**
         * Crea o actualiza la evaluacion clinica de una cita.
         * Si ya existe una evaluacion para esa cita, la actualiza en lugar de crear una
         * nueva.
         */
        @Transactional
        public EvaluacionClinicaResponseDTO guardarEvaluacion(EvaluacionClinicaRequestDTO request) {

                Cita cita = citaRepository.findById(request.getIdCita())
                                .orElseThrow(() -> new RuntimeException(
                                                "Cita no encontrada con id: " + request.getIdCita()));

                // Buscamos si ya existe una evaluacion para esta cita para no duplicar
                EvaluacionClinica evaluacion = evaluacionRepository.findByCita_IdCitas(request.getIdCita())
                                .orElse(new EvaluacionClinica());

                evaluacion.setCita(cita);
                evaluacion.setDiagnostico(request.getDiagnostico());
                evaluacion.setObservaciones(request.getObservaciones());

                EvaluacionClinica guardada = evaluacionRepository.save(evaluacion);
                return new EvaluacionClinicaResponseDTO(
                                guardada.getIdEvaluacionClinica(),
                                guardada.getCita().getIdCitas(),
                                guardada.getDiagnostico(),
                                guardada.getObservaciones());
        }

        /**
         * Obtiene la evaluacion clinica de una cita si existe.
         * El frontend la llama al abrir una consulta para saber si ya hay datos
         * guardados.
         */
        public EvaluacionClinicaResponseDTO obtenerEvaluacionPorCita(Integer idCita) {
                return evaluacionRepository.findByCita_IdCitas(idCita)
                                .map(e -> new EvaluacionClinicaResponseDTO(
                                                e.getIdEvaluacionClinica(),
                                                e.getCita().getIdCitas(),
                                                e.getDiagnostico(),
                                                e.getObservaciones()))
                                .orElse(null);
        }

        // =========================================================================
        // PASO 2: PLANES DE TRATAMIENTO (hallazgos del odontograma)
        // =========================================================================

        /**
         * Registra un hallazgo en el odontograma como un plan de tratamiento.
         * Cada hallazgo referencia una evaluacion clinica, un tratamiento y una pieza
         * dental.
         */
        @Transactional
        public PlanTratamientoResponseDTO registrarHallazgo(PlanTratamientoRequestDTO request) {

                EvaluacionClinica evaluacion = evaluacionRepository.findById(request.getIdEvaluacionClinica())
                                .orElseThrow(() -> new RuntimeException("Evaluacion clinica no encontrada."));

                Tratamiento tratamiento = tratamientoRepository.findById(request.getIdTratamiento())
                                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado."));

                PlanTratamiento plan = new PlanTratamiento();
                plan.setEvaluacionClinica(evaluacion);
                plan.setTratamiento(tratamiento);
                plan.setPiezaDental(request.getPiezaDental());

                // Si no viene estado, usamos PENDIENTE como valor por defecto
                try {
                        plan.setEstadoPlan(request.getEstadoPlan() != null ? EstadoPlan.valueOf(request.getEstadoPlan())
                                        : EstadoPlan.PENDIENTE);
                } catch (IllegalArgumentException e) {
                        plan.setEstadoPlan(EstadoPlan.PENDIENTE);
                }

                PlanTratamiento guardado = planTratamientoRepository.save(plan);
                return toResponseDTO(guardado);
        }

        /**
         * Obtiene todos los hallazgos registrados en una evaluacion clinica.
         * Usado para cargar el panel lateral de hallazgos al abrir la consulta.
         */
        public List<PlanTratamientoResponseDTO> obtenerHallazgosPorEvaluacion(Integer idEvaluacion) {
                return planTratamientoRepository
                                .findByEvaluacionClinica_IdEvaluacionClinica(idEvaluacion)
                                .stream()
                                .map(this::toResponseDTO)
                                .collect(Collectors.toList());
        }

        /**
         * Elimina un hallazgo del odontograma.
         */
        @Transactional
        public void eliminarHallazgo(Integer idPlan) {
                if (!planTratamientoRepository.existsById(idPlan))
                        throw new RuntimeException("Plan de tratamiento no encontrado.");
                planTratamientoRepository.deleteById(idPlan);
        }

        /**
         * Actualiza el estado de un hallazgo (PlanTratamiento) específico.
         * * @param idPlan El ID del plan de tratamiento / hallazgo.
         * 
         * @param nuevoEstado El nuevo estado (ej. "EN_PROCESO", "FINALIZADO").
         * 
         * @return PlanTratamientoResponseDTO con los datos actualizados.
         */
        @Transactional
        public PlanTratamientoResponseDTO actualizarEstadoHallazgo(Integer idPlan, String nuevoEstado) {

                // 1. Buscamos el hallazgo
                PlanTratamiento plan = planTratamientoRepository.findById(idPlan)
                                .orElseThrow(() -> new RuntimeException(
                                                "No se encontró el hallazgo con ID: " + idPlan));

                try {
                        // 2. Convertimos el string que viene de React a tu Enum EstadoPlan
                        EstadoPlan estadoEnum = EstadoPlan
                                        .valueOf(nuevoEstado != null ? nuevoEstado.trim().toUpperCase() : "");

                        // 3. Obtenemos el estado actual (ahora devuelve el Enum, no un String)
                        EstadoPlan estadoActual = plan.getEstadoPlan();

                        // Comparamos directamente los Enums usando "=="
                        if ((estadoActual == EstadoPlan.COMPLETADO || estadoActual == EstadoPlan.CANCELADO)
                                        && estadoActual != estadoEnum) {
                                throw new IllegalStateException(
                                                "No se puede modificar un hallazgo que ya está " + estadoActual);
                        }

                        // 4. AQUÍ ESTÁ LA CORRECCIÓN: Le pasamos el Enum directamente
                        plan.setEstadoPlan(estadoEnum);

                        // 5. Guardamos en DB
                        plan = planTratamientoRepository.save(plan);

                        return toResponseDTO(plan);

                } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Estado no válido. Los estados permitidos son: "
                                        + Arrays.toString(EstadoPlan.values()));
                }
        }

        // =========================================================================
        // PASO 3: PRESCRIPCION
        // =========================================================================

        /**
         * Crea una prescripcion con sus detalles de medicamentos para una cita.
         * Si ya existe una prescripcion para la cita, lanza error para evitar
         * duplicados.
         */
        @Transactional
        public PrescripcionResponseDTO crearPrescripcion(PrescripcionRequestDTO request) {

                Cita cita = citaRepository.findById(request.getIdCita())
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

                // Validamos que no exista ya una prescripcion para esta cita
                if (prescripcionRepository.findByCita_IdCitas(request.getIdCita()).isPresent()) {
                        throw new RuntimeException("Ya existe una prescripcion para esta cita.");
                }

                // Creamos la cabecera de la prescripcion
                Prescripcion prescripcion = new Prescripcion();
                prescripcion.setCita(cita);
                prescripcion.setFechaPrescripcion(LocalDateTime.now());
                Prescripcion prescripcionGuardada = prescripcionRepository.save(prescripcion);

                // Creamos cada linea de detalle (un medicamento por linea)
                List<DetallePrescripcion> detalles = request.getDetalles().stream().map(dto -> {
                        Medicamento medicamento = medicamentoRepository.findById(dto.getIdMedicamento())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Medicamento no encontrado: " + dto.getIdMedicamento()));

                        DetallePrescripcion detalle = new DetallePrescripcion();
                        detalle.setPrescripcion(prescripcionGuardada);
                        detalle.setMedicamento(medicamento);
                        detalle.setDosisPrescripcion(dto.getDosis());
                        detalle.setFrecuenciaPrescripcion(dto.getFrecuencia());
                        detalle.setDuracionPrescripcion(dto.getDuracion());
                        detalle.setIndicacionesPrescripcion(dto.getIndicaciones());
                        return detalle;
                }).collect(Collectors.toList());

                detalleRepository.saveAll(detalles);

                // Retornamos la prescripcion completa con sus detalles mapeados
                return mapearPrescripcion(prescripcionGuardada, detalles);
        }

        /**
         * Obtiene la prescripcion de una cita si ya fue generada.
         * Usado al abrir el paso 3 para saber si ya hay una receta guardada.
         */
        public PrescripcionResponseDTO obtenerPrescripcionPorCita(Integer idCita) {
                return prescripcionRepository.findByCita_IdCitas(idCita)
                                .map(p -> {
                                        List<DetallePrescripcion> detalles = detalleRepository
                                                        .findByPrescripcion_IdPrescripcion(p.getIdPrescripcion());
                                        return mapearPrescripcion(p, detalles);
                                })
                                .orElse(null);
        }

        // =========================================================================
        // CATALOGOS: Tratamientos y Medicamentos para los selects del frontend
        // =========================================================================

        public List<TratamientoResponseDTO> listarTratamientos() {
                return tratamientoRepository.findAll().stream()
                                .map(t -> new TratamientoResponseDTO(
                                                t.getIdTratamiento(),
                                                t.getNombreTratamiento(),
                                                t.getDescripcionTratamiento(),
                                                t.getCostoTratamiento()))
                                .collect(Collectors.toList());
        }

        @Transactional
        public TratamientoResponseDTO crearTratamiento(TratamientoRequestDTO request) {

                // Construimos la entidad con los datos del request
                Tratamiento nuevo = new Tratamiento();
                nuevo.setNombreTratamiento(request.getNombreTratamiento());
                nuevo.setDescripcionTratamiento(request.getDescripcionTratamiento());
                nuevo.setCostoTratamiento(request.getCostoTratamiento());

                Tratamiento guardado = tratamientoRepository.save(nuevo);

                return new TratamientoResponseDTO(
                                guardado.getIdTratamiento(),
                                guardado.getNombreTratamiento(),
                                guardado.getDescripcionTratamiento(),
                                guardado.getCostoTratamiento());
        }

        public List<MedicamentoResponseDTO> listarMedicamentos() {
                return medicamentoRepository.findAll().stream()
                                .map(m -> new MedicamentoResponseDTO(
                                                m.getIdMedicamento(),
                                                m.getNombreMedicamento(),
                                                m.getComponenteActivo(),
                                                m.getConcentracion(),
                                                m.getCostoMedicamento(),
                                                m.getCantidadInventario()))
                                .collect(Collectors.toList());
        }

        // =========================================================================
        // METODOS PRIVADOS DE MAPEO
        // =========================================================================

        private PlanTratamientoResponseDTO toResponseDTO(PlanTratamiento plan) {
                return new PlanTratamientoResponseDTO(
                                plan.getIdPlanTratamiento(),
                                plan.getPiezaDental(),
                                plan.getEstadoPlan().name(),
                                plan.getTratamiento().getIdTratamiento(),
                                plan.getTratamiento().getNombreTratamiento(),
                                plan.getTratamiento().getDescripcionTratamiento(),
                                plan.getTratamiento().getCostoTratamiento().floatValue());
        }

        private PrescripcionResponseDTO mapearPrescripcion(Prescripcion p, List<DetallePrescripcion> detalles) {
                List<PrescripcionResponseDTO.DetallePrescripcionResponseDTO> detallesDTO = detalles.stream()
                                .map(d -> new PrescripcionResponseDTO.DetallePrescripcionResponseDTO(
                                                d.getIdDetallePrescripcion(),
                                                d.getMedicamento().getIdMedicamento(),
                                                d.getMedicamento().getNombreMedicamento(),
                                                d.getMedicamento().getComponenteActivo(),
                                                d.getMedicamento().getConcentracion(),
                                                d.getDosisPrescripcion(),
                                                d.getFrecuenciaPrescripcion(),
                                                d.getDuracionPrescripcion(),
                                                d.getIndicacionesPrescripcion(),
                                                // Evita error 500 si el plan viene nulo ---
                                                d.getPlanTratamiento() != null
                                                                ? d.getPlanTratamiento().getIdPlanTratamiento()
                                                                : null

                                ))
                                .collect(Collectors.toList());

                return new PrescripcionResponseDTO(
                                p.getIdPrescripcion(),
                                p.getCita().getIdCitas(),
                                p.getFechaPrescripcion(),
                                detallesDTO);
        }

}
