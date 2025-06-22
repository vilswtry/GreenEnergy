package com.GreenEnergy.coordinacionRecursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenEnergy.coordinacionRecursos.model.MaterialAsignadoMantencion;

public interface MaterialAsignadoMantencionRepository extends JpaRepository<MaterialAsignadoMantencion, Long> {
    List<MaterialAsignadoMantencion> findByMantencionId(Long mantencionId);

}
