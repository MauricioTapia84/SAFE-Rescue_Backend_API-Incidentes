package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.Ubicacion;
import com.SAFE_Rescue.API_Incidentes.repository.UbicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con compañías de bomberos.
 * <p>
 * Proporciona métodos para CRUD de compañías, validación de reglas de negocio
 * y comunicación con servicios externos para obtener información de ubicaciones.
 * </p>
 */
@Service
public class UbicacionService {

    @Autowired
    private CompaniaRepository companiaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todas las compañías registradas.
     * @return Lista de todas las compañías
     */
    public List<Ubicacion> findAll() {
        return companiaRepository.findAll();
    }

    /**
     * Busca una compañía por su ID.
     * @param id Identificador único de la compañía
     * @return La compañía encontrada
     * @throws NoSuchElementException Si no se encuentra la compañía
     */
    public Ubicacion findByID(long id) {
        return companiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Compañía no encontrada con ID: " + id));
    }

    /**
     * Guarda una nueva compañía en el sistema con validación de ubicación.
     * @param ubicacion Compañía a guardar
     * @return Compañía guardada con ID generado
     * @throws IllegalArgumentException Si la compañía no pasa las validaciones
     * @throws RuntimeException Si ocurre un error al guardar o validar la ubicación
     */
    public Ubicacion save(Ubicacion ubicacion) {
        try{

            validarCompania(ubicacion);

            return companiaRepository.save(ubicacion);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Ubicacion: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza una compañía existente con validación de datos.
     * @param ubicacion Datos actualizados de la compañía
     * @param id ID de la compañía a actualizar
     * @return Compañía actualizada
     * @throws NoSuchElementException Si no se encuentra la compañía con el ID especificado
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public Ubicacion update(Ubicacion ubicacion, long id) {
        Ubicacion antiguaUbicacion = companiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Compañía no encontrada con ID: " + id));

        validarCompania(ubicacion);

        if (ubicacion.getNombre() != null) {
            antiguaUbicacion.setNombre(ubicacion.getNombre());
        }

        if (ubicacion.getUbicacion1() != null) {
            antiguaUbicacion.setUbicacion1(ubicacion.getUbicacion1());
        }

        return companiaRepository.save(antiguaUbicacion);
    }

    /**
     * Elimina una compañía por su ID.
     * @param id ID de la compañía a eliminar
     * @throws NoSuchElementException Si no se encuentra la compañía
     */
    public void delete(long id) {
        if (!companiaRepository.existsById(id)) {
            throw new NoSuchElementException("Compañía no encontrada con ID: " + id);
        }
        companiaRepository.deleteById(id);
    }


    //Validaciones

    /**
     * Valida los datos básicos de una compañía.
     * @param ubicacion Compañía a validar
     * @throws IllegalArgumentException Si la compañía no cumple con las reglas de validación
     */
    private void validarCompania(Ubicacion ubicacion) {
        if (ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la compañía es requerido");
        }
        if (ubicacion.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }

        validarUbicacion(ubicacion.getUbicacion1());
    }

    /**
     * Valida los datos de una ubicación.
     * @param ubicacion1 Ubicación a validar
     * @throws IllegalArgumentException Si la ubicación no cumple con las reglas de validación
     */
    private void validarUbicacion(Ubicacion1 ubicacion1) {

        //numeracion
        if (ubicacion1.getNumeracion() <= 0) {
            throw new IllegalArgumentException("La numeración debe ser un número positivo");
        } else {
            if (String.valueOf(ubicacion1.getNumeracion()).length()> 5) {
                throw new RuntimeException("El valor de la Numeración excede máximo de caracteres (5)");
            }
        }

        //calle
        if (ubicacion1.getCalle() != null) {
            if (ubicacion1.getCalle().length() > 50) {
                throw new RuntimeException("El nombre de la calle no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la calle es requerido");
        }

        //comuna
        if (ubicacion1.getComuna() != null) {
            if (ubicacion1.getComuna().length() > 50) {
                throw new RuntimeException("El nombre de la comuna no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la comuna es requerido");
        }

        //region
        if (ubicacion1.getRegion() != null) {
            if (ubicacion1.getRegion().length() > 50) {
                throw new RuntimeException("El nombre de la Región no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la Región es requerido");
        }

    }


    // MÉTODOS DE ASIGNACIÓN y OBTENCION DE RELACIONES

    /**
     * Asigna una ubicación a una compañía existente.
     * @param companiaId ID de la compañía a actualizar
     * @param ubicacionId ID de la ubicación a asignar
     * @throws NoSuchElementException Si no se encuentra la compañía o la ubicación
     * @throws IllegalArgumentException Si la ubicación no pasa las validaciones
     */
    public void asignarUbicacion(Long companiaId, Long ubicacionId) {
        Ubicacion ubicacion = findByID(companiaId);
        Ubicacion1 ubicacion1 = obtenerUbicacion(ubicacionId);
        validarUbicacion(ubicacion1);
        ubicacion.setUbicacion1(ubicacion1);
        companiaRepository.save(ubicacion);
    }

    /**
     * Obtiene una ubicación por su ID.
     * @param ubicacionId ID de la ubicación a buscaSr
     * @return Ubicación encontrada
     * @throws RuntimeException Si no se encuentra la ubicación con el ID especificado
     */
    public Ubicacion1 obtenerUbicacion(Long ubicacionId) {
        return ubicacionRepository.findById(ubicacionId)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + ubicacionId));
    }

}