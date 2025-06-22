package com.GreenEnergy.coordinacionRecursos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.coordinacionRecursos.model.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    @Query("SELECT t FROM Tecnico t WHERE t.especialidad = :especialidad AND :fecha NOT IN elements(t.fechasOcupadas)")
    List<Tecnico> findByEspecialidadAndFechaDisponible(@Param("especialidad") String especialidad,
            @Param("fecha") LocalDate fecha);

}
