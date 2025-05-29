package com.SAFE_Rescue.API_Incidentes.controller;

import com.SAFE_Rescue.API_Incidentes.modelo.Incidente;
import com.SAFE_Rescue.API_Incidentes.service.IncidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de equipos de emergencia.
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de equipos.
 */
@RestController
@RequestMapping("/api-turnos/v1/equipos")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los equipos registrados en el sistema.
     * @return ResponseEntity con lista de equipos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Incidente>> listar(){

        List<Incidente> incidentes = incidenteService.findAll();
        if(incidentes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(incidentes);
    }

    /**
     * Busca un equipo por su ID.
     * @param id ID del equipo a buscar
     * @return ResponseEntity con el equipo encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEquipo(@PathVariable long id) {
        Incidente incidente;

        try {
            incidente = incidenteService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Incidente no encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(incidente);

    }

    /**
     * Crea un nuevo incidente.
     * @param incidente Datos del incidente a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarEquipo(@RequestBody Incidente incidente) {
        try {
            incidenteService.save(incidente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Incidente creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un incidente existente.
     * @param id ID del incidente a actualizar
     * @param incidente Datos actualizados del incidente
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarEquipo(@PathVariable long id, @RequestBody Incidente incidente) {
        try {
            Incidente nuevoIncidente = incidenteService.update(incidente, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Incidente no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un equipo del sistema.
     * @param id ID del equipo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEquipo(@PathVariable long id) {

        incidenteService.delete(id);
        return ResponseEntity.ok("Incidente eliminado con éxito.");
    }


    // GESTIÓN DE RELACIONES

    /**
     * Asigna una compañía a un equipo.
     * @param equipoId ID del equipo
     * @param companiaId ID de la compañía a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{equipoId}/asignar-compania/{companiaId}")
    public ResponseEntity<String> asignarCompania(@PathVariable int equipoId, @PathVariable int companiaId) {
        try {
            incidenteService.asignarCompania(equipoId, companiaId);
            return ResponseEntity.ok("Compañia asignada al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un turno a un equipo.
     * @param equipoId ID del equipo
     * @param turnoId ID del turno a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{equipoId}/asignar-turno/{turnoId}")
    public ResponseEntity<String> asignarTurno(@PathVariable int equipoId, @PathVariable int turnoId) {
        try {
            incidenteService.asignarTurno(equipoId, turnoId);
            return ResponseEntity.ok("Equipo asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un tipo de equipo a un equipo.
     * @param equipoId ID del equipo
     * @param tipoEquipoId ID del tipo de equipo a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{equipoId}/asignar-tipo-equipo/{tipoEquipoId}")
    public ResponseEntity<String> asignarTipoEquipo(@PathVariable int equipoId, @PathVariable int tipoEquipoId) {
        try {
            incidenteService.asignarTipoEquipo(equipoId, tipoEquipoId);
            return ResponseEntity.ok("Tipo Incidente asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna una lista de bomberos a un equipo.
     * @param equipoId ID del equipo
     * @param bomberosId Lista de IDs de bomberos a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{equipoId}/asignar-personal/{bomberosId}")
    public ResponseEntity<String> asignaPersonal(@PathVariable int equipoId, @PathVariable List<Long> bomberosId) {
        try {
            incidenteService.asignarListaBomberos(equipoId, bomberosId);
            return ResponseEntity.ok("Lista bomberos asignada al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
