package com.SAFE_Rescue.API_Incidentes.repository;

import com.SAFE_Rescue.API_Incidentes.modelo.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {


}

