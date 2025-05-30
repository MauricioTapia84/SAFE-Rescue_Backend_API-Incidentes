package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.TipoIncidente;
import com.SAFE_Rescue.API_Incidentes.repository.TipoIncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con tipos de incidentes.
 * Proporciona métodos para CRUD de tipos de incidentes y validación de reglas de negocio.
 */
@Service
public class TipoIncidenteService {

    @Autowired
    private TipoIncidenteRepository tipoIncidenteRepository;

    // MÉTODOS CRUD PRINCIPALES
    /**
     * Obtiene todos los tipos de incidentes registrados.
     * @return Lista de todos los tipos de incidentes
     */
    public List<TipoIncidente> findAll() {
        return tipoIncidenteRepository.findAll();
    }

    /**
     * Busca un tipo de incidente por su ID.
     * @param id Identificador único del tipo de incidente
     * @return El tipo de incidente encontrado
     * @throws NoSuchElementException Si no se encuentra el tipo de incidente
     */
    public TipoIncidente findByID(long id) {
        return tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de incidente no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo tipo de incidente.
     * @param tipoIncidente tipo de incidente a guardar
     * @return tipo de incidente guardado
     * @throws IllegalArgumentException Si el tipo de incidente no pasa las validaciones
     */
    public TipoIncidente save(TipoIncidente tipoIncidente) {
        try{
            validarTipoIncidente(tipoIncidente);
            return tipoIncidenteRepository.save(tipoIncidente);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Tipo Incidente: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un tipo de incidente existente.
     * @param tipoIncidente Datos actualizados del tipo de incidente
     * @param id ID del tipo de incidente a actualizar
     * @return tipo de incidente actualizado
     * @throws NoSuchElementException Si no se encuentra el tipo de incidente
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public TipoIncidente update(TipoIncidente tipoIncidente, long id) {
        TipoIncidente tipoExistente = tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de incidente no encontrado con ID: " + id));

        if (tipoIncidente.getNombre() != null) {
            tipoExistente.setNombre(tipoIncidente.getNombre());
        }
        validarTipoIncidente(tipoIncidente);

        return tipoIncidenteRepository.save(tipoExistente);
    }

    /**
     * Elimina un tipo de incidente por su ID.
     * @param id ID del tipo de incidente a eliminar
     * @throws NoSuchElementException Si no se encuentra el tipo de incidente
     */
    public void delete(long id) {
        if (!tipoIncidenteRepository.existsById(id)) {
            throw new NoSuchElementException("Tipo de incidente no encontrado con ID: " + id);
        }
        tipoIncidenteRepository.deleteById(id);
    }

    /**
     * Valida los datos de un tipo de incidente.
     * @param tipoIncidente tipo de incidente a validar
     * @throws IllegalArgumentException Si el tipo de incidente no cumple con las reglas de validación
     */
    private void validarTipoIncidente(TipoIncidente tipoIncidente) {
        if (tipoIncidente.getNombre() == null || tipoIncidente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del Tipo de incidente es requerido");
        }
        if (tipoIncidente.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
    }

}