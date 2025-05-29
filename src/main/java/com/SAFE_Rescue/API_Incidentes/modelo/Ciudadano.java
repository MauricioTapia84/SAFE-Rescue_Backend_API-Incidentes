package com.SAFE_Rescue.API_Incidentes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ciudadano")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ciudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true,length = 8,nullable = false)
    private Long run;

    @Column(length = 1,nullable = false)
    private String dv;

    @Column(length = 50,nullable = false)
    private String nombre;

    @Column(length = 50,nullable = false)
    private String a_paterno;

    @Column(length = 50,nullable = false)
    private String a_materno;

    @Column(unique = true,length = 9,nullable = false)
    private Long telefono;

}