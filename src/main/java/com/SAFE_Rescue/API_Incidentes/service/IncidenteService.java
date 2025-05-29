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
 * Servicio para la gestión integral de equipos de emergencia.
 * Maneja operaciones CRUD, asignación de recursos y personal,
 * y validación de datos para equipos de rescate.
 */
@Service
@Transactional
public class IncidenteService {

    // REPOSITORIOS INYECTADOS
    @Autowired private IncidenteRepository incidenteRepository;
    @Autowired private CiudadanoRepository ciudadanoRepository;
    @Autowired private CompaniaRepository companiaRepository;
    @Autowired private TipoIncidenteRepository tipoIncidenteRepository;
    @Autowired private EquipoRepository equipoRepository;
    @Autowired private RecursoRepository recursoRepository;
    @Autowired private EstadoIncidenteRepository estadoIncidenteRepository;

    // SERVICIOS INYECTADOS
    @Autowired private EstadoIncidenteService estadoIncidenteService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private TipoIncidenteService tipoIncidenteService;


    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los equipos registrados en el sistema.
     * @return Lista completa de equipos
     */
    public List<Incidente> findAll() {
        return incidenteRepository.findAll();
    }

    /**
     * Busca un equipo por su ID único.
     * @param id Identificador del equipo
     * @return Incidente encontrado
     * @throws NoSuchElementException Si no se encuentra el equipo
     */
    public Incidente findByID(long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró equipo con ID: " + id));
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
            Equipo equipoGuardado = estadoIncidenteService.save(incidente.getEquipo());
            Ubicacion ubicacionGuardada = ubicacionService.save(incidente.getUbicacion());
            TipoIncidente tipoIncidenteGuardado = tipoIncidenteService.save(incidente.getTipoIncidente());

            incidente.setEquipo(equipoGuardado);
            incidente.setUbicacion(ubicacionGuardada);
            incidente.setTipoIncidente(tipoIncidenteGuardado);

            // Asignación de recursos asociados
            asignarVehiculosAlEquipo(incidente);
            asignarBomberosAlEquipo(incidente);
            asignarRecursosAlEquipo(incidente);

            validarEquipo(incidente);

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
            if (incidente.getEstadoIncidentes() != null) {
                asignarVehiculosAlEquipo(incidente);
                incidenteExistente.setEstadoIncidentes(incidente.getEstadoIncidentes());
            }

            if (incidente.getPersonal() != null) {
                asignarBomberosAlEquipo(incidente);
                incidenteExistente.setPersonal(incidente.getPersonal());
            }

            if (incidente.getRecursos() != null) {
                asignarRecursosAlEquipo(incidente);
                incidenteExistente.setRecursos(incidente.getRecursos());
            }

            validarEquipo(incidenteExistente);
            return incidenteRepository.save(incidenteExistente);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar incidente: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un equipo del sistema.
     * @param id Identificador del equipo a eliminar
     * @throws NoSuchElementException Si no se encuentra el equipo
     */
    public void delete(long id) {
        if (!incidenteRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró equipo con ID: " + id);
        }
        incidenteRepository.deleteById(id);
    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna una compañía a un equipo.
     * @param equipoId ID del equipo
     * @param companiaId ID de la compañía
     */
    public void asignarCompania(long equipoId, long companiaId) {
        Incidente incidente = incidenteRepository.findById(equipoId)
            .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        Ubicacion ubicacion = companiaRepository.findById(companiaId)
            .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));
        incidente.setUbicacion(ubicacion);
        incidenteRepository.save(incidente);

    }

    /**
     * Asigna un tipo de equipo a un equipo.
     * @param equipoId ID del equipo
     * @param tipoEquipoId ID del tipo de equipo
     */
    public void asignarTipoEquipo(long equipoId, long tipoEquipoId) {
        Incidente incidente = incidenteRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Tipo Incidente no encontrado"));
        incidente.setTipoIncidente(tipoIncidente);
        incidenteRepository.save(incidente);
    }

    /**
     * Asigna un turno a un equipo.
     * @param equipoId ID del equipo
     * @param turnoId ID del turno
     */
    public void asignarTurno(long equipoId, long turnoId) {
        Incidente incidente = incidenteRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado"));
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        incidente.setEquipo(equipo);
        incidenteRepository.save(incidente);
    }

    /**
     * Asigna una lista de bomberos a un equipo.
     * @param equipoId ID del equipo
     * @param bomberosIds Lista de IDs de bomberos
     * @throws IllegalArgumentException Si la lista es nula o vacía
     */
    public void asignarListaBomberos(long equipoId, List<Long> bomberosIds) {
        if (bomberosIds == null || bomberosIds.isEmpty()) {
            throw new IllegalArgumentException("La lista de bomberos no puede estar vacía");
        }

        Incidente incidente = incidenteRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("No se encontró incidente con ID: " + equipoId));

        incidente.setPersonal(obtenerPersonal(bomberosIds));
        incidenteRepository.save(incidente);
    }
    /**
     * Asigna bomberos a un incidente obteniéndolos de la base de datos
     * @param incidente Incidente al que se asignarán los bomberos
     */
    private void asignarBomberosAlEquipo(Incidente incidente) {
        if (incidente.getPersonal() != null && !incidente.getPersonal().isEmpty()) {
            List<Long> bomberosIds = extraerIdsDeBomberos(incidente.getPersonal());
            incidente.setPersonal(obtenerPersonal(bomberosIds));
        }
    }

    /**
     * Asigna recursos a un incidente obteniéndolos de la base de datos
     * @param incidente Incidente al que se asignarán los recursos
     */
    private void asignarRecursosAlEquipo(Incidente incidente) {
        if (incidente.getRecursos() != null && !incidente.getRecursos().isEmpty()) {
            List<Long> recursosIds = extraerIdsRecursos(incidente.getRecursos());
            incidente.setRecursos(obtenerRecursos(recursosIds));
        }
    }

    /**
     * Asigna vehículos a un incidente obteniéndolos de la base de datos
     * @param incidente Incidente al que se asignarán los vehículos
     */
    private void asignarVehiculosAlEquipo(Incidente incidente) {
        if (incidente.getEstadoIncidentes() != null && !incidente.getEstadoIncidentes().isEmpty()) {
            List<Long> vehiculosIds = extraerIdsVehiculos(incidente.getEstadoIncidentes());
            incidente.setEstadoIncidentes(obtenerVehiculos(vehiculosIds));
        }
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    private void validarEquipo(Incidente incidente) {

        if (incidente.getNombre() != null) {
            if (incidente.getNombre().length() > 50) {
                throw new RuntimeException("El nombre no puede exceder 50 caracteres");
            }
        }

        if (incidente.getCantidadMiembros() != null) {
            if (String.valueOf(incidente.getCantidadMiembros()).length()> 5) {
                throw new RuntimeException("El valor de la Cantidad de miembros excede máximo de caracteres (2)");
            }
        }

        if (incidente.getLider() != null) {
            if (incidente.getLider().length() > 50) {
                throw new RuntimeException("El nombre del líder no puede exceder 50 caracteres");
            }
        }

    }

    private void actualizarRelaciones(Incidente fuente, Incidente destino) {
        if (fuente.getEquipo() != null) {
            destino.setEquipo(estadoIncidenteService.save(fuente.getEquipo()));
        }
        if (fuente.getUbicacion() != null) {
            destino.setUbicacion(ubicacionService.save(fuente.getUbicacion()));
        }
        if (fuente.getTipoIncidente() != null) {
            destino.setTipoIncidente(tipoIncidenteService.save(fuente.getTipoIncidente()));
        }
    }


    // MÉTODOS PARA CONSULTA

    /**
     * Obtiene información de bomberos desde la base de datos.
     * @param bomberosIds Lista de IDs de bomberos
     * @return Lista con información de bomberos
     * @throws RuntimeException Si hay error en la comunicación
     */
    private List<Ciudadano> obtenerPersonal(List<Long> bomberosIds) {
        if (bomberosIds == null || bomberosIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<Ciudadano> ciudadanos = new ArrayList<>();
            for (Long id : bomberosIds) {
                Ciudadano ciudadano = ciudadanoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado con ID: " + id));
                ciudadanos.add(ciudadano);
            }
            return ciudadanos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener lista de bomberos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene información de vehículos desde base de datos
     * @param vehiculosIds Lista de IDs de vehículos
     * @return Lista con información de vehículos
     * @throws RuntimeException Si hay error en la comunicación
     */
    private List<EstadoIncidente> obtenerVehiculos(List<Long> vehiculosIds) {
        if (vehiculosIds == null || vehiculosIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<EstadoIncidente> estadoIncidentes = new ArrayList<>();
            for (int i =0;vehiculosIds.size()>i;i++) {
                EstadoIncidente estadoIncidente = estadoIncidenteRepository.findById(vehiculosIds.get(i))
                        .orElseThrow(() -> new RuntimeException("EstadoIncidente no encontrado"));
                estadoIncidentes.add(estadoIncidente);
            }
            return estadoIncidentes;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener lista de vehículos: " + e.getMessage(), e);
        }

    }

    /**
     * Obtiene información de recursos desde base de datos
     * @param recursosIds Lista de IDs de recursos
     * @return Lista con información de recursos
     * @throws RuntimeException Si hay error en la comunicación
     */
    private List<Recurso> obtenerRecursos(List<Long> recursosIds) {
        if (recursosIds == null || recursosIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<Recurso> recursos = new ArrayList<>();
            for (int i =0;recursosIds.size()>i;i++) {
                Recurso recurso = recursoRepository.findById(recursosIds.get(i))
                        .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
                recursos.add(recurso);
            }
            return recursos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener lista de Recursos: " + e.getMessage(), e);
        }
    }

    private List<Long> extraerIdsDeBomberos(List<Ciudadano> ciudadanos) {
        if (ciudadanos == null || ciudadanos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = new ArrayList<>();
        for (Ciudadano ciudadano : ciudadanos) {
            ids.add(Long.valueOf(ciudadano.getId()));
        }
        return ids;
    }

    private List<Long> extraerIdsVehiculos(List<EstadoIncidente> estadoIncidentes) {
        if (estadoIncidentes == null || estadoIncidentes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = new ArrayList<>();
        for (EstadoIncidente estadoIncidente : estadoIncidentes) {
            ids.add(Long.valueOf(estadoIncidente.getId()));
        }
        return ids;
    }

    private List<Long> extraerIdsRecursos(List<Recurso> recursos) {
        if (recursos == null || recursos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = new ArrayList<>();
        for (Recurso recurso: recursos) {
            ids.add(Long.valueOf(recurso.getId()));
        }
        return ids;
    }

}