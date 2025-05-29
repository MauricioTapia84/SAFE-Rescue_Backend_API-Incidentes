package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ubicacion")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ubicacion {

    /**
     * Identificador único de la ubicación en el sistema
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nombre de la calle o avenida
     * Ejemplo: "Av. Libertador Bernardo O'Higgins"
     */
    @Column(length = 50, nullable = false)
    private String calle;

    /**
     * Número de la dirección
     * Debe ser un valor positivo
     * Ejemplo: 1234
     */
    @Column(unique = true, length = 5, nullable = false)
    private int numeracion;

    /**
     * Comuna o distrito de la ubicación
     * Ejemplo: "Santiago Centro"
     */
    @Column(length = 50, nullable = false)
    private String comuna;

    /**
     * Región o estado/provincia
     * Ejemplo: "Región Metropolitana"
     */
    @Column(length = 50, nullable = false)
    private String region;
}