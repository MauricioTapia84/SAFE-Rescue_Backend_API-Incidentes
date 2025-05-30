package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.*;
import com.SAFE_Rescue.API_Incidentes.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de Incidente de emergencia.
 * Maneja operaciones CRUD, asignación de recursos y personal,
 * y validación de datos para Incidente de rescate.
 */
@Service
@Transactional
public class IncidenteService {

    // REPOSITORIOS INYECTADOS
    @Autowired private IncidenteRepository incidenteRepository;
    @Autowired private UbicacionRepository UbicacionRepository;
    @Autowired private CiudadanoRepository ciudadanoRepository;
    @Autowired private TipoIncidenteRepository tipoIncidenteRepository;
    @Autowired private EquipoRepository equipoRepository;
    @Autowired private EstadoIncidenteRepository estadoIncidenteRepository;

    // SERVICIOS INYECTADOS
    @Autowired private EstadoIncidenteService estadoIncidenteService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private TipoIncidenteService tipoIncidenteService;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los Incidentes registrados en el sistema.
     * @return Lista completa de Incidentes
     */
    public List<Incidente> findAll() {
        return incidenteRepository.findAll();
    }

    /**
     * Busca un Incidente por su ID único.
     * @param id Identificador del Incidente
     * @return Incidente encontrado
     * @throws NoSuchElementException Si no se encuentra el equipo
     */
    public Incidente findByID(long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró Incidente con ID: " + id));
    }

    /**
     * Guarda un nuevo incidente en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param incidente Datos del incidente a guardar
     * @return Incidente guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     */
    public Incidente save(Incidente incidente) {
        try {
            // Validación y persistencia de relaciones principales
            Ciudadano ciudadanoGuardado = ciudadanoRepository.save(incidente.getCiudadano());
            Equipo equipoGuardado = equipoRepository.save(incidente.getEquipo());
            EstadoIncidente estadoIncidenteGuardado = estadoIncidenteService.save(incidente.getEstadoIncidente());
            Ubicacion ubicacionGuardada = ubicacionService.save(incidente.getUbicacion());
            TipoIncidente tipoIncidenteGuardado = tipoIncidenteService.save(incidente.getTipoIncidente());

            incidente.setEquipo(equipoGuardado);
            incidente.setUbicacion(ubicacionGuardada);
            incidente.setTipoIncidente(tipoIncidenteGuardado);
            incidente.setCiudadano(ciudadanoGuardado);
            incidente.setEstadoIncidente(estadoIncidenteGuardado);

            validarIncidente(incidente);

            return incidenteRepository.save(incidente);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el incidente: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un incidente existente.
     * @param incidente Datos actualizados del incidente
     * @param id Identificador del incidente a actualizar
     * @return Incidente actualizado
     * @throws IllegalArgumentException Si el incidente proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el incidente a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Incidente update(Incidente incidente, long id) {
        if (incidente == null) {
            throw new IllegalArgumentException("El incidente no puede ser nulo");
        }

        Incidente incidenteExistente = incidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Incidente no encontrado con ID: " + id));

        try {
            actualizarRelaciones(incidente, incidenteExistente);

            // Actualizar recursos asociados
            if (incidente.getEstadoIncidente() != null) {
                asignarEstadoIncidente(Long.valueOf(incidente.getId()),Long.valueOf(incidente.getEstadoIncidente().getId()));
                incidenteExistente.setEstadoIncidente(incidente.getEstadoIncidente());
            }

            if (incidente.getEquipo() != null) {
                asignarEquipo(Long.valueOf(incidente.getId()),Long.valueOf(incidente.getEquipo().getId()));
                incidenteExistente.setEquipo(incidente.getEquipo());
            }

            if (incidente.getCiudadano() != null) {
                asignarCiudadano(Long.valueOf(incidente.getId()),Long.valueOf(incidente.getCiudadano().getId()));
                incidenteExistente.setCiudadano(incidente.getCiudadano());
            }

            if (incidente.getTipoIncidente() != null) {
                asignarTipoIncidente(Long.valueOf(incidente.getId()),Long.valueOf(incidente.getTipoIncidente().getId()));
                incidenteExistente.setTipoIncidente(incidente.getTipoIncidente());
            }

            if (incidente.getUbicacion() != null) {
                asignarUbicacion(Long.valueOf(incidente.getId()),Long.valueOf(incidente.getUbicacion().getId()));
                incidenteExistente.setUbicacion(incidente.getUbicacion());
            }

            validarIncidente(incidenteExistente);
            return incidenteRepository.save(incidenteExistente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar incidente: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un incidente del sistema.
     * @param id Identificador del incidente a eliminar
     * @throws NoSuchElementException Si no se encuentra el incidente
     */
    public void delete(long id) {
        if (!incidenteRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró incidente con ID: " + id);
        }
        incidenteRepository.deleteById(id);
    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna una Ciudadano a un incidente.
     * @param incidenteId ID del incidente
     * @param ciudadanoId ID de la incidente
     */
    public void asignarCiudadano(long incidenteId, long ciudadanoId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
            .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        Ciudadano ciudadano = ciudadanoRepository.findById(ciudadanoId)
            .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado"));
        incidente.setCiudadano(ciudadano);
        incidenteRepository.save(incidente);

    }

    /**
     * Asigna un tipo de incidente a un incidente.
     * @param incidenteId ID del incidente
     * @param tipoIncidenteId ID del tipo de incidente
     */
    public void asignarTipoIncidente(long incidenteId, long tipoIncidenteId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(tipoIncidenteId)
                .orElseThrow(() -> new RuntimeException("Tipo Incidente no encontrado"));
        incidente.setTipoIncidente(tipoIncidente);
        incidenteRepository.save(incidente);
    }

    /**
     * Asigna un Estado de incidente a un incidente.
     * @param incidenteId ID del incidente
     * @param estadoIncidenteId ID del Estado Incidente
     */
    public void asignarEstadoIncidente(long incidenteId, long estadoIncidenteId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        EstadoIncidente estadoIncidente = estadoIncidenteRepository.findById(estadoIncidenteId)
                .orElseThrow(() -> new RuntimeException("Estado Incidente no encontrado"));
        incidente.setEstadoIncidente(estadoIncidente);
        incidenteRepository.save(incidente);
    }

    /**
     * Asigna un Equipo a un incidente.
     * @param incidenteId ID del incidente
     * @param equipoId ID del Equipo
     */
    public void asignarEquipo(long incidenteId, long equipoId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        incidente.setEquipo(equipo);
        incidenteRepository.save(incidente);
    }

    /**
     * Asigna un Ubicacion a un incidente.
     * @param incidenteId ID del incidente
     * @param ubicacionId ID del Ubicacion
     */
    public void asignarUbicacion(long incidenteId, long ubicacionId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        Ubicacion ubicacion = UbicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrado"));
        incidente.setUbicacion(ubicacion);
        incidenteRepository.save(incidente);
    }


    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    private void validarIncidente(Incidente incidente) {

        if (incidente.getTitulo() != null) {
            if (incidente.getTitulo().length() > 50) {
                throw new RuntimeException("El Titulo no puede exceder 50 caracteres");
            }
        }

        if (incidente.getDetalle() != null) {
            if (incidente.getDetalle().length() > 400) {
                throw new RuntimeException("El detalle no puede exceder 400 caracteres");
            }
        }

    }

    private void actualizarRelaciones(Incidente fuente, Incidente destino) {
        if (fuente.getEstadoIncidente() != null) {
            destino.setEstadoIncidente(estadoIncidenteService.save(fuente.getEstadoIncidente()));
        }
        if (fuente.getUbicacion() != null) {
            destino.setUbicacion(ubicacionService.save(fuente.getUbicacion()));
        }
        if (fuente.getTipoIncidente() != null) {
            destino.setTipoIncidente(tipoIncidenteService.save(fuente.getTipoIncidente()));
        }
    }

}


