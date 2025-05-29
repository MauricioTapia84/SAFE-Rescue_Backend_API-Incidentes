package com.SAFE_Rescue.API_Incidentes.controller;

import com.SAFE_Rescue.API_Incidentes.modelo.Equipo;
import com.SAFE_Rescue.API_Incidentes.service.EstadoIncidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para gestionar operaciones relacionadas con turnos.
 * Proporciona endpoints para listar, crear, buscar, actualizar y eliminar turnos.
 */
@RestController
@RequestMapping("/api-turnos/v1/turnos")
public class EstadoIncidenteController {

    @Autowired
    private EstadoIncidenteService estadoIncidenteService;

    /**
     * Obtiene la lista de todos los turnos existentes.
     *
     * @return ResponseEntity con la lista de turnos si existen,
     *         o código de estado NO_CONTENT (204) si la lista está vacía.
     */
    @GetMapping
    public ResponseEntity<List<Equipo>> listar(){
        List<Equipo> equipos = estadoIncidenteService.findAll();
        if(equipos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(equipos);
    }

    /**
     * Crea un nuevo equipo.
     *
     * @param equipo El objeto Equipo a crear, recibido en el cuerpo de la petición.
     * @return ResponseEntity con mensaje de éxito y código CREATED (201) si se crea correctamente,
     *         BAD_REQUEST (400) si hay errores de validación,
     *         o INTERNAL_SERVER_ERROR (500) si ocurre un error inesperado.
     */
    @PostMapping
    public ResponseEntity<String> agregarTurno(@RequestBody Equipo equipo) {
        try {
            estadoIncidenteService.validarTurno(equipo);
            Equipo nuevoEquipo = estadoIncidenteService.save(equipo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Equipo creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Busca un turno por su ID.
     *
     * @param id El ID del turno a buscar.
     * @return ResponseEntity con el turno encontrado si existe,
     *         o NOT_FOUND (404) si no se encuentra el turno.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTurno(@PathVariable long id) {
        Equipo equipo;
        try {
            equipo = estadoIncidenteService.findByID(id);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<String>("Equipo no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(equipo);
    }

    /**
     * Actualiza un equipo existente.
     *
     * @param id El ID del equipo a actualizar.
     * @param equipo El objeto Equipo con los nuevos datos.
     * @return ResponseEntity con mensaje de éxito si se actualiza correctamente,
     *         NOT_FOUND (404) si no se encuentra el equipo,
     *         BAD_REQUEST (400) si hay errores de validación,
     *         o INTERNAL_SERVER_ERROR (500) si ocurre un error inesperado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTurno(@PathVariable long id, @RequestBody Equipo equipo) {
        try {
            Equipo nuevoEquipo = estadoIncidenteService.update(equipo, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Equipo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un turno existente.
     *
     * @param id El ID del turno a eliminar.
     * @return ResponseEntity con mensaje de éxito si se elimina correctamente,
     *         NOT_FOUND (404) si no se encuentra el turno,
     *         BAD_REQUEST (400) si hay errores en la operación,
     *         o INTERNAL_SERVER_ERROR (500) si ocurre un error inesperado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable long id) {
        try {
            estadoIncidenteService.delete(id);
            return ResponseEntity.ok("Equipo eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Equipo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }
}