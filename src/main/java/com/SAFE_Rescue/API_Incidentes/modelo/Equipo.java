package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un equipo de bomberos en el sistema.
 * Contiene información sobre la composición y estado del equipo.
 */
@Entity
@Table(name = "equipo") // Nombre de la tabla en la base de datos
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
@Data // Genera getters, setters, toString, equals y hashCode
public class Equipo {

    /**
     * Identificador único del equipo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    private int id;

    /**
     * Nombre del equipo (máximo 50 caracteres)
     */
    @Column(name = "nombre_equipo", length = 50, nullable = false)
    private String nombre;

    /**
     * Estado actual del equipo (activo/inactivo)
     */
    @Column(nullable = false)
    private boolean estado;

    /**
     * Nombre del líder del equipo (máximo 50 caracteres)
     */
    @Column(name = "nombre_lider", length = 50, nullable = true)
    private String lider;

}