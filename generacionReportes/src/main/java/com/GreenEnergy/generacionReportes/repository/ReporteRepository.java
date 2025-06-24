package com.GreenEnergy.generacionReportes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.generacionReportes.model.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByFechaCreacion(LocalDate fechaCreacion);

    List<Reporte> findByIdProyectoMantencion(Long proyectoId);

}
