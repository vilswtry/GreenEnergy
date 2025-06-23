package com.GreenEnergy.soporteTecnico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;
import com.GreenEnergy.soporteTecnico.model.Usuario;
import com.GreenEnergy.soporteTecnico.repository.TicketSoporteRepository;
import com.GreenEnergy.soporteTecnico.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TicketSoporteService {

    private final TicketSoporteRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    public List<TicketSoporte> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public List<TicketSoporte> buscarTicketsPorEstado(TicketSoporte.EstadoTicket estado) {
        return ticketRepository.findByEstado(estado);
    }

    public TicketSoporteService(TicketSoporteRepository ticketRepository, UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public TicketSoporte crearTicket(Long usuarioId, String asunto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRol() == Usuario.Rol.TECNICO_SOPORTE) {
            throw new RuntimeException("Los técnicos de soporte no pueden abrir tickets.");
        }

        Optional<Usuario> tecnicoSoporte = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Usuario.Rol.TECNICO_SOPORTE)
                .findFirst();

        TicketSoporte ticket = new TicketSoporte();
        ticket.setUsuario(usuario);
        ticket.setAsunto(asunto);
        ticket.setDescripcion(descripcion);
        tecnicoSoporte.ifPresent(ticket::setTecnicoSoporte);
        ticket.setEstado(TicketSoporte.EstadoTicket.CREADO);

        return ticketRepository.save(ticket);
    }

    public List<TicketSoporte> listarTicketsPorUsuario(Long usuarioId) {
        return ticketRepository.findByUsuarioId(usuarioId);
    }

    public List<TicketSoporte> listarTicketsPorTecnicoSoporte(Long tecnicoSoporteId) {
        return ticketRepository.findByTecnicoSoporteId(tecnicoSoporteId);
    }

    public TicketSoporte responderTicket(Long ticketId, Long tecnicoSoporteId, String respuesta) {
        TicketSoporte ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        if (ticket.getTecnicoSoporte() == null || !ticket.getTecnicoSoporte().getId().equals(tecnicoSoporteId)) {
            throw new RuntimeException("No tienes permiso para responder este ticket");
        }

        ticket.setRespuesta(respuesta);
        ticket.setEstado(TicketSoporte.EstadoTicket.EN_PROGRESO);

        return ticketRepository.save(ticket);
    }

    public TicketSoporte cerrarTicket(Long ticketId, Long tecnicoSoporteId) {
        TicketSoporte ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        if (ticket.getTecnicoSoporte() == null || !ticket.getTecnicoSoporte().getId().equals(tecnicoSoporteId)) {
            throw new RuntimeException("No tienes permiso para cerrar este ticket");
        }

        ticket.setEstado(TicketSoporte.EstadoTicket.CERRADO);

        return ticketRepository.save(ticket);
    }
}
