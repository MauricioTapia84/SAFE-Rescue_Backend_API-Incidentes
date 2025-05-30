package com.SAFE_Rescue.API_Incidentes.controller;


import com.SAFE_Rescue.API_Incidentes.modelo.EstadoIncidente;
import com.SAFE_Rescue.API_Incidentes.service.EstadoIncidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de estado de Incidente de emergencia.
 * Proporciona endpoints para operaciones CRUD de estado de Incidente.
 */
@RestController
@RequestMapping("/api-incidentes/v1/estados-incidentes")
public class EstadoIncidenteController {

    @Autowired
    private EstadoIncidenteService estadoIncidenteService;

    /**
     * Obtiene todos los estados de incidentes registrados en el sistema.
     * @return ResponseEntity con lista de estado de incidente o retornaNO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<EstadoIncidente>> listarEstadoIncidente() {
        List<EstadoIncidente> estadoIncidente = estadoIncidenteService.findAll();
        if(estadoIncidente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(estadoIncidente);
    }

    /**
     * Busca un Estado de incidente por su ID.
     * @param id ID del Estado de incidente a buscar
     * @return ResponseEntity con el Estado de incidente encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEstadoIncidente(@PathVariable int id) {
        EstadoIncidente estadoIncidente;
        try {
            estadoIncidente = estadoIncidenteService.findByID(id);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<String>("Estado Incidente no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estadoIncidente);
    }

    /**
     * Crea un nuevo Estado de incidente.
     * @param estadoIncidente Datos del Estado de incidente a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarEstadoIncidente(@RequestBody EstadoIncidente estadoIncidente) {
        try {
            estadoIncidenteService.save(estadoIncidente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estado Incidente creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un Estado de incidente existente.
     * @param id ID del Estado de incidente a actualizar
     * @param estadoIncidente Datos actualizados del Estado de incidente
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEstadoIncidente(@PathVariable long id, @RequestBody EstadoIncidente estadoIncidente) {
        try {
            EstadoIncidente nuevoEstadoIncidente = estadoIncidenteService.update(estadoIncidente, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estado Incidente no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un Estado de incidente del sistema.
     * @param id ID del Estado de incidente a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEstadoIncidente(@PathVariable long id) {
        try {
            estadoIncidenteService.delete(id);
            return ResponseEntity.ok("Estado Incidente eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estado Incidente no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }
}