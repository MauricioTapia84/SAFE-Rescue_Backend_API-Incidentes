package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "estado_incidente")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstadoIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "nombre_estado", length = 50, nullable = false)
    private String nombre;

}