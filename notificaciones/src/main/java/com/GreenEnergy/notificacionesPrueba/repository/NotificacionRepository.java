package com.GreenEnergy.notificacionesPrueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.notificacionesPrueba.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

List<Notificacion> findByUsuarioId(Long usuarioId);
}
