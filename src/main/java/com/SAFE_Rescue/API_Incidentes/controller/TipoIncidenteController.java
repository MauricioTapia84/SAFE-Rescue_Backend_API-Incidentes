package com.SAFE_Rescue.API_Incidentes.controller;

import com.SAFE_Rescue.API_Incidentes.modelo.TipoIncidente;
import com.SAFE_Rescue.API_Incidentes.service.TipoIncidenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de tipos de Incidente de emergencia.
 * Proporciona endpoints para operaciones CRUD de tipos de Incidente.
 */
@RestController
@RequestMapping("/api-incidentes/v1/tipos-incidentes")
public class TipoIncidenteController {

    @Autowired
    private TipoIncidenteService tipoIncidenteService;

    /**
     * Obtiene todos los tipos de incidente registrados en el sistema.
     * @return ResponseEntity con lista de tipos de incidente o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<TipoIncidente>> listarTiposIncidente() {
        List<TipoIncidente> tipoIncidente = tipoIncidenteService.findAll();
        if(tipoIncidente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(tipoIncidente);
    }

    /**
     * Busca un tipo de incidente por su ID.
     * @param id ID del tipo de incidente a buscar
     * @return ResponseEntity con el tipo de incidente encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoIncidente(@PathVariable int id) {
        TipoIncidente tipoIncidente;
        try {
            tipoIncidente = tipoIncidenteService.findByID(id);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<String>("Tipo Incidente no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipoIncidente);
    }

    /**
     * Crea un nuevo tipo de incidente.
     * @param tipoIncidente Datos del tipo de incidente a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarTipoIncidente(@RequestBody TipoIncidente tipoIncidente) {
        try {
            tipoIncidenteService.save(tipoIncidente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tipo Incidente creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un tipo de incidente existente.
     * @param id ID del tipo de incidente a actualizar
     * @param tipoIncidente Datos actualizados del tipo de incidente
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTipoIncidente(@PathVariable long id, @RequestBody TipoIncidente tipoIncidente) {
        try {
            TipoIncidente nuevoTipoIncidente = tipoIncidenteService.update(tipoIncidente, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tipo Incidente no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un tipo de incidente del sistema.
     * @param id ID del tipo de incidente a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipoIncidente(@PathVariable long id) {
        try {
            tipoIncidenteService.delete(id);
            return ResponseEntity.ok("Tipo Incidente eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tipo Incidente no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }
}
