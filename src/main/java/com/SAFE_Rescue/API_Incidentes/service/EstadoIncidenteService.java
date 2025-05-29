package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.EstadoIncidente;
import com.SAFE_Rescue.API_Incidentes.repository.EstadoIncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con los Estados de Incidente.
 * Proporciona métodos para CRUD y validaciones de Estado Incidente.
 */
@Service
@Transactional
public class EstadoIncidenteService {

    @Autowired
    private EstadoIncidenteRepository estadoIncidenteRepository;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los Estados de Incidente existentes.
     * @return Lista de todos los Estados de Incidente
     */
    public List<EstadoIncidente> findAll() {
        return estadoIncidenteRepository.findAll();
    }

    /**
     * Busca un EstadovIncidente por su ID.
     * @param id ID del Estado Incidente a buscar
     * @return El Estado Incidente encontrado
     * @throws NoSuchElementException Si no se encuentra el Estado Incidente
     */
    public EstadoIncidente findByID(long id) {
        return estadoIncidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estado Incidente con ID " + id + " no encontrado"));
    }

    /**
     * Guarda un nuevo Estado Incidente después de validarlo.
     * @param estadoIncidente Estado Incidente a guardar
     * @return Estado Incidente guardado
     * @throws RuntimeException Si hay errores de validación o al guardar
     */
    public EstadoIncidente save(EstadoIncidente estadoIncidente) {
        try {
            validarEstadoIncidente(estadoIncidente);
            return estadoIncidenteRepository.save(estadoIncidente);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Estado Incidente: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un Estado Incidente existente.
     * @param estadoIncidente Estado Incidente con los nuevos datos
     * @param id ID del Estado Incidente a actualizar
     * @return Estado Incidente actualizado
     * @throws RuntimeException Si hay errores de validación o al actualizar
     */
    public EstadoIncidente update(EstadoIncidente estadoIncidente, long id) {
        try {
            EstadoIncidente antiguoEstado = estadoIncidenteRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Equipo no encontrado"));

            // Actualización de campos con validación
            if (estadoIncidente.getDetalle() != null) {
                if (estadoIncidente.getDetalle().length() > 50) {
                    throw new RuntimeException("El valor detalle excede máximo de caracteres (50)");
                }
                antiguoEstado.setDetalle(estadoIncidente.getDetalle());
            }

            return estadoIncidenteRepository.save(antiguoEstado);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el Estado Incidente: " + e.getMessage());
        }
    }

    /**
     * Elimina un Estado Incidente por su ID.
     * @param id ID del Estado Incidente a eliminar
     * @throws RuntimeException Si no se encuentra el Estado Incidente o hay error al eliminar
     */
    public void delete(long id) {
        try {
            if (!estadoIncidenteRepository.existsById(id)) {
                throw new NoSuchElementException("Estado Incidente no encontrado");
            }
            estadoIncidenteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar Estado Incidente: " + e.getMessage());
        }
    }

    /**
     * Valida los datos de un EstadoIncidente antes de guardarlo o actualizarlo.
     * @param estadoIncidente EstadoIncidente a validar
     * @throws RuntimeException Si alguna validación falla
     */
    public void validarEstadoIncidente(@NotNull EstadoIncidente estadoIncidente) {
        if (estadoIncidente.getDetalle() == null || estadoIncidente.getDetalle().isEmpty()) {
            throw new RuntimeException("El nombre del equipo es requerido");
        }

        if (estadoIncidente.getDetalle().length() > 50) {
            throw new RuntimeException("El valor Detalle excede máximo de caracteres (50)");
        }

    }

}