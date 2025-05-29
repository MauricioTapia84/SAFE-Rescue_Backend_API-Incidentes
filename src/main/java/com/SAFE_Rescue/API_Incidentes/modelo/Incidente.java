package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Entity
@Table(name = "incidente") // Nombre de la tabla en la base de datos
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
@Data // Genera getters, setters, toString, equals y hashCode
public class Incidente {

    /**
     * Identificador único del incidente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    private int id;

    @Column(length = 50, nullable = false)
    private String titulo;

    @Column(length = 400, nullable = true)
    private String detalle;

    /**
     * Tipo de incidente
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "tipo_incidente_id", referencedColumnName = "id")
    private TipoIncidente tipoIncidente;

    /**
     * Ubicacion del incidente
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion ubicacion;

    /**
     * Ciudadano que reporta el incidente
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "ciudadano_id", referencedColumnName = "id")
    private Ciudadano ciudadano;

    /**
     * Estado en el que se encuentra el incidente
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "estado_incidente_id", referencedColumnName = "id")
    private EstadoIncidente estadoIncidente;

    /**
     * Equipo asignado al equipo
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "equipo_id", referencedColumnName = "id")
    private Equipo equipo;

}
