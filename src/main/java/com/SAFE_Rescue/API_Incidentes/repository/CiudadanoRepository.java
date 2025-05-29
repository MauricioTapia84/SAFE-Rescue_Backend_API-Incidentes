package com.SAFE_Rescue.API_Incidentes.repository;

import com.SAFE_Rescue.API_Incidentes.modelo.Ciudadano;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long>{

}



