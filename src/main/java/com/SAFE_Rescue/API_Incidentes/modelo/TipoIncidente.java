package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los diferentes tipos de incidentes en el sistema.
 * <p>
 * Cada tipo de incidente define una categorización particular para los incidentes reportados,
 * permitiendo agruparlos por características o funciones específicas.
 * </p>
 *
 * @see Incidente
 */
@Entity
@Table(name = "tipo_incidente")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoIncidente {

    /**
     * Identificador único autoincremental del tipo de equipo.
     * <p>
     * Se genera automáticamente mediante la estrategia IDENTITY de la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nombre descriptivo del tipo de incidente.
     * <p>
     * Restricciones:
     * </p>
     * <ul>
     *   <li>Máximo 50 caracteres</li>
     *   <li>No puede ser nulo</li>
     *   <li>Se almacena en la columna 'nombre_tipo'</li>
     * </ul>
     *
     * <p>Ejemplos: "Incidente de Rescate", "Incidente de Primeros Auxilios", "Incidente de Materiales Peligrosos"</p>
     */
    @Column(name = "nombre_tipo", length = 50, nullable = false)
    private String nombre;

}