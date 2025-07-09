package com.GreenEnergy.coordinacionRecursos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.coordinacionRecursos.model.Especialidad;
import com.GreenEnergy.coordinacionRecursos.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.especialidad = :especialidad AND :fecha NOT MEMBER OF u.fechasOcupadas")
    List<Usuario> findByEspecialidadAndFechaDisponible(
            @Param("especialidad") Especialidad especialidad,
            @Param("fecha") LocalDate fecha);
}
