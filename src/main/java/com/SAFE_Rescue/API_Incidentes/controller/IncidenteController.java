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
 * Controlador REST para la gestión de incidentes
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de incidentes
 */
@RestController
@RequestMapping("/api-incidentes/v1/incidentes")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los incidentes registrados en el sistema.
     * @return ResponseEntity con lista de incidentes o estado NO_CONTENT si no hay registros
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
     * Busca un incidente por su ID.
     * @param id ID del incidente a buscar
     * @return ResponseEntity con el incidente encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarIncidente(@PathVariable long id) {
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
    public ResponseEntity<String> agregarIncidente(@RequestBody Incidente incidente) {
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
    public ResponseEntity<String> actualizarIncidente(@PathVariable long id, @RequestBody Incidente incidente) {
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
     * Elimina un incidente del sistema.
     * @param id ID del incidente a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarIncidente(@PathVariable long id) {
        try {
            incidenteService.delete(id);
            return ResponseEntity.ok("Incidente eliminado con éxito.");
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


    // GESTIÓN DE RELACIONES

    /**
     * Asigna un ciudadano un incidente.
     * @param incidenteId ID del incidente
     * @param ciudadanoId del ciudadano
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{incidenteId}/asignar-ciudadano/{ciudadanoId}")
    public ResponseEntity<String> asignacCiudadano(@PathVariable Long incidenteId, @PathVariable int ciudadanoId) {
        try {
            incidenteService.asignarCiudadano(incidenteId,ciudadanoId);
            return ResponseEntity.ok("Ciudadano asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un estado de incidente a un incidente
     * @param incidenteId ID del incidente
     * @param estadoIncidenteId ID del estado de incidente a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{incidenteId}/asignar-estado-incidente/{estadoIncidenteId}")
    public ResponseEntity<String> asignarEstadoIncidente(@PathVariable int incidenteId, @PathVariable int estadoIncidenteId) {
        try {
            incidenteService.asignarEstadoIncidente(incidenteId,estadoIncidenteId);
            return ResponseEntity.ok("Estado Incidente asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un tipo de incidente a un incidente
     * @param incidenteId ID del incidente
     * @param tipoIncidenteId ID del tipo de incidente a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{incidenteId}/asignar-tipo-incidente/{tipoIncidenteId}")
    public ResponseEntity<String> asignarTipoIncidente(@PathVariable int incidenteId, @PathVariable int tipoIncidenteId) {
        try {
            incidenteService.asignarTipoIncidente(incidenteId,tipoIncidenteId);
            return ResponseEntity.ok("Tipo Incidente asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna una equipo a un incidente.
     * @param incidenteId ID del incidente
     * @param equipoId del equipo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{incidenteId}/asignar-equipo/{equipoId}")
    public ResponseEntity<String> asignaEquipo(@PathVariable Long incidenteId, @PathVariable int equipoId) {
        try {
            incidenteService.asignarEquipo(incidenteId,equipoId);
            return ResponseEntity.ok("Equipo asignado al Incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna una ubicacion a un incidente.
     * @param incidenteId ID del incidente
     * @param ubicacionId ID de la Ubicacion
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{incidenteId}/asignar-ubicacion/{ubicacionId}")
    public ResponseEntity<String> asignarUbicacion(@PathVariable Long incidenteId, @PathVariable Long ubicacionId) {
        try {
            incidenteService.asignarUbicacion(incidenteId, ubicacionId);
            return ResponseEntity.ok("Ubicacion asignada al incidente exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
