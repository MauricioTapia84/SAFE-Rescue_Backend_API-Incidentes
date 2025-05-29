package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.TipoIncidente;
import com.SAFE_Rescue.API_Incidentes.repository.TipoIncidenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con tipos de equipo de bomberos.
 * Proporciona métodos para CRUD de tipos de equipo y validación de reglas de negocio.
 */
@Service
public class TipoIncidenteService {

    @Autowired
    private TipoIncidenteRepository tipoIncidenteRepository;

    // MÉTODOS CRUD PRINCIPALES
    /**
     * Obtiene todos los tipos de equipo registrados.
     * @return Lista de todos los tipos de equipo
     */
    public List<TipoIncidente> findAll() {
        return tipoIncidenteRepository.findAll();
    }

    /**
     * Busca un tipo de equipo por su ID.
     * @param id Identificador único del tipo de equipo
     * @return El tipo de equipo encontrado
     * @throws NoSuchElementException Si no se encuentra el tipo de equipo
     */
    public TipoIncidente findByID(long id) {
        return tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de equipo no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo tipo de equipo.
     * @param tipoIncidente Tipo de equipo a guardar
     * @return Tipo de equipo guardado
     * @throws IllegalArgumentException Si el tipo de equipo no pasa las validaciones
     */
    public TipoIncidente save(TipoIncidente tipoIncidente) {
        try{
            validarTipoEquipo(tipoIncidente);
            return tipoIncidenteRepository.save(tipoIncidente);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el TipoIncidente: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un tipo de equipo existente.
     * @param tipoIncidente Datos actualizados del tipo de equipo
     * @param id ID del tipo de equipo a actualizar
     * @return Tipo de equipo actualizado
     * @throws NoSuchElementException Si no se encuentra el tipo de equipo
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public TipoIncidente update(TipoIncidente tipoIncidente, long id) {
        TipoIncidente tipoExistente = tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de equipo no encontrado con ID: " + id));

        if (tipoIncidente.getNombre() != null) {
            validarNombreTipoEquipo(tipoIncidente.getNombre());
            tipoExistente.setNombre(tipoIncidente.getNombre());
        }

        return tipoIncidenteRepository.save(tipoExistente);
    }

    /**
     * Elimina un tipo de equipo por su ID.
     * @param id ID del tipo de equipo a eliminar
     * @throws NoSuchElementException Si no se encuentra el tipo de equipo
     */
    public void delete(long id) {
        if (!tipoIncidenteRepository.existsById(id)) {
            throw new NoSuchElementException("Tipo de equipo no encontrado con ID: " + id);
        }
        tipoIncidenteRepository.deleteById(id);
    }

    /**
     * Valida los datos de un tipo de equipo.
     * @param tipoIncidente Tipo de equipo a validar
     * @throws IllegalArgumentException Si el tipo de equipo no cumple con las reglas de validación
     */
    private void validarTipoEquipo(TipoIncidente tipoIncidente) {
        if (tipoIncidente.getNombre() == null || tipoIncidente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del Tipo Equipoa es requerido");
        }
        if (tipoIncidente.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
    }

    /**
     * Valida el nombre de un tipo de equipo.
     * @param nombre Nombre a validar
     * @throws IllegalArgumentException Si el nombre excede el límite de caracteres
     */
    private void validarNombreTipoEquipo(String nombre) {
        if (nombre.length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
    }
}