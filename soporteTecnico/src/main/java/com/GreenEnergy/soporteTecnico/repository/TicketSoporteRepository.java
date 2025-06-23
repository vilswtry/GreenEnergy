package com.GreenEnergy.soporteTecnico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;

@Repository
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {

    List<TicketSoporte> findByUsuarioId(Long usuarioId);

    List<TicketSoporte> findByTecnicoSoporteId(Long tecnicoSoporteId);

    List<TicketSoporte> findByEstado(TicketSoporte.EstadoTicket estado);
}