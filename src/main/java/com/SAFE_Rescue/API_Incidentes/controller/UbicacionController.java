package com.SAFE_Rescue.API_Incidentes.controller;

import com.SAFE_Rescue.API_Incidentes.modelo.Ubicacion;
import com.SAFE_Rescue.API_Incidentes.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de ubicaciones
 * Proporciona endpoints para operaciones CRUD de ubicaciones
 */
@RestController
@RequestMapping("/api-incidente/v1/ubicaciones")
public class UbicacionController {

    @Autowired
    private UbicacionService ubicacionService;

    /**
     * Obtiene todas las ubicaciones registradas en el sistema.
     * @return ResponseEntity con lista de ubicaciones o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Ubicacion>> listarUbicaiones(){

        List<Ubicacion> ubicacion = ubicacionService.findAll();
        if(ubicacion.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(ubicacion);
    }

    /**
     * Busca una Ubicacion por su ID.
     * @param id ID de la Ubicacion a buscar
     * @return ResponseEntity con la Ubicacion encontrada o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUbicacion(@PathVariable int id) {
        Ubicacion ubicacion;

        try {
            ubicacion = ubicacionService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Ubicacion no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ubicacion);

    }

    /**
     * Crea una nueva Ubicacion
     * @param ubicacion Datos de la Ubicacion a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarUbicacion(@RequestBody Ubicacion ubicacion) {
        try {
            ubicacionService.save(ubicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ubicacion creada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una Ubicacion existente.
     * @param id ID de la Ubicacion a actualizar
     * @param ubicacion Datos actualizados de la Ubicacion
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUbicacion(@PathVariable long id, @RequestBody Ubicacion ubicacion) {
        try {
            Ubicacion nuevoUbicacion = ubicacionService.update(ubicacion, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ubicacion no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una Ubicacion del sistema.
     * @param id ID de la Ubicacion a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUbicacion(@PathVariable long id) {
        ubicacionService.delete(id);
        return ResponseEntity.ok("Ubicacion eliminada con éxito.");
    }


}
