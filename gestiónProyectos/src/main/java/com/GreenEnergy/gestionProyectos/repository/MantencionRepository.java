package com.GreenEnergy.gestionProyectos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenEnergy.gestionProyectos.model.Mantencion;
import com.GreenEnergy.gestionProyectos.model.Mantencion.EstadoMantencion;
import com.GreenEnergy.gestionProyectos.model.Proyecto;

public interface MantencionRepository extends JpaRepository<Mantencion, Long> {

    List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado);

    Optional<Mantencion> findByNombreAndFechaMantencion(String nombre, LocalDate fechaMantencion);

    List<Mantencion> findByEstadoNotAndFechaMantencionBefore(EstadoMantencion estado, LocalDate fecha);

}
