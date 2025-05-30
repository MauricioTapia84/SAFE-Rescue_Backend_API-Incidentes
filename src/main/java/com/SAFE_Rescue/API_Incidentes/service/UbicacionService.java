package com.SAFE_Rescue.API_Incidentes.service;

import com.SAFE_Rescue.API_Incidentes.modelo.Ubicacion;
import com.SAFE_Rescue.API_Incidentes.repository.UbicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con Ubicacion.
 * <p>
 * Proporciona métodos para CRUD de Ubicacion, validación de reglas de negocio.
 * </p>
 */
@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todas las Ubicacion registradas.
     * @return Lista de todas las Ubicacion
     */
    public List<Ubicacion> findAll() {
        return ubicacionRepository.findAll();
    }

    /**
     * Busca una Ubicacion por su ID.
     * @param id Identificador único de la Ubicacion
     * @return La Ubicacion encontrada
     * @throws NoSuchElementException Si no se encuentra la Ubicacion
     */
    public Ubicacion findByID(long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ubicacion no encontrada con ID: " + id));
    }

    /**
     * Guarda una nueva Ubicacion en el sistema con validación de ubicación.
     * @param ubicacion Ubicacion a guardar
     * @return Ubicacion guardada con ID generado
     * @throws IllegalArgumentException Si la Ubicacion no pasa las validaciones
     * @throws RuntimeException Si ocurre un error al guardar o validar la ubicación
     */
    public Ubicacion save(Ubicacion ubicacion) {
        try{

            validarUbicacion(ubicacion);

            return ubicacionRepository.save(ubicacion);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Ubicacion: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza una Ubicacion existente con validación de datos.
     * @param ubicacion Datos actualizados de la Ubicacion
     * @param id ID de la Ubicacion a actualizar
     * @return Ubicacion actualizada
     * @throws NoSuchElementException Si no se encuentra la Ubicacion con el ID especificado
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public Ubicacion update(Ubicacion ubicacion, long id) {
        Ubicacion antiguaUbicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ubicacion no encontrada con ID: " + id));

        validarUbicacion(ubicacion);

        if (ubicacion.getCalle() != null) {
            antiguaUbicacion.setCalle(ubicacion.getCalle());
        }

        if (ubicacion.getNumeracion() != 0) {
            antiguaUbicacion.setNumeracion(ubicacion.getNumeracion());
        }

        if (ubicacion.getComuna() != null) {
            antiguaUbicacion.setComuna(ubicacion.getComuna());
        }

        if (ubicacion.getRegion() != null) {
            antiguaUbicacion.setRegion(ubicacion.getRegion());
        }

        return ubicacionRepository.save(antiguaUbicacion);
    }

    /**
     * Elimina una Ubicacion por su ID.
     * @param id ID de la Ubicacion a eliminar
     * @throws NoSuchElementException Si no se encuentra la Ubicacion
     */
    public void delete(long id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new NoSuchElementException("Ubicacion no encontrada con ID: " + id);
        }
        ubicacionRepository.deleteById(id);
    }


    //Validaciones

    /**
     * Valida los datos de una ubicación.
     * @param ubicacion Ubicación a validar
     * @throws IllegalArgumentException Si la ubicación no cumple con las reglas de validación
     */
    private void validarUbicacion(Ubicacion ubicacion) {

        //numeracion
        if (ubicacion.getNumeracion() <= 0) {
            throw new IllegalArgumentException("La numeración debe ser un número positivo");
        } else {
            if (String.valueOf(ubicacion.getNumeracion()).length()> 5) {
                throw new RuntimeException("El valor de la Numeración excede máximo de caracteres (5)");
            }
        }

        //calle
        if (ubicacion.getCalle() != null) {
            if (ubicacion.getCalle().length() > 50) {
                throw new RuntimeException("El nombre de la calle no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la calle es requerido");
        }

        //comuna
        if (ubicacion.getComuna() != null) {
            if (ubicacion.getComuna().length() > 50) {
                throw new RuntimeException("El nombre de la comuna no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la comuna es requerido");
        }

        //region
        if (ubicacion.getRegion() != null) {
            if (ubicacion.getRegion().length() > 50) {
                throw new RuntimeException("El nombre de la Región no puede exceder 50 caracteres");
            }
        }else{
            throw new IllegalArgumentException("El nombre de la Región es requerido");
        }

    }


}