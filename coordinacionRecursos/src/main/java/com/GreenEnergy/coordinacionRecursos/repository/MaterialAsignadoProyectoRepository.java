package com.GreenEnergy.coordinacionRecursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenEnergy.coordinacionRecursos.model.MaterialAsignadoProyecto;

public interface MaterialAsignadoProyectoRepository extends JpaRepository<MaterialAsignadoProyecto, Long> {
    List<MaterialAsignadoProyecto> findByProyectoId(Long proyectoId);

}
