package com.GreenEnergy.backupRestoreService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.backupRestoreService.model.EstadoSistema;

@Repository
public interface EstadoSistemaRepository extends JpaRepository<EstadoSistema, Long> {

    Optional<EstadoSistema> findTopByOrderByFechaDesc();

}
