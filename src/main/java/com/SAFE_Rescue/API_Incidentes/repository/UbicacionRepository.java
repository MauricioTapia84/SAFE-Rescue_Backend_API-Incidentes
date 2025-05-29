package com.SAFE_Rescue.API_Incidentes.repository;

import com.SAFE_Rescue.API_Incidentes.modelo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

}
