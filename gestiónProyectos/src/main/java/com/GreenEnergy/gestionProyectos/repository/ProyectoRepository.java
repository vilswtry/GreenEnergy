package com.GreenEnergy.gestionProyectos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.gestionProyectos.model.Proyecto;
import com.GreenEnergy.gestionProyectos.model.Proyecto.EstadoProyecto;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado);

    List<Proyecto> findByEstadoInAndFechaFinBefore(List<Proyecto.EstadoProyecto> estados, LocalDate fecha);

    List<Proyecto> findByEstadoInAndFechaFinAfter(List<Proyecto.EstadoProyecto> estados, LocalDate fecha);

    List<Proyecto> findByFechaInicioBeforeAndFechaFinAfter(LocalDate hoy1, LocalDate hoy2);

    Optional<Proyecto> findByNombreAndFechaInicioAndFechaFin(String nombre, LocalDate fechaInicio, LocalDate fechaFin);

    List<Proyecto> findByEstadoNotAndFechaFinBefore(EstadoProyecto estado, LocalDate fecha);

}
