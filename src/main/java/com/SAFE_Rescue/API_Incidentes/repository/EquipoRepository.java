package com.SAFE_Rescue.API_Incidentes.repository;

import com.SAFE_Rescue.API_Incidentes.modelo.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {


}