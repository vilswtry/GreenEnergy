package com.GreenEnergy.soporteTecnico.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;
import com.GreenEnergy.soporteTecnico.model.Usuario;
import com.GreenEnergy.soporteTecnico.repository.TicketSoporteRepository;
import com.GreenEnergy.soporteTecnico.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TicketSoporteServiceTest {

    @Mock
    private TicketSoporteRepository ticketRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TicketSoporteService ticketService;

    private Usuario cliente;
    private Usuario tecnicoSoporte;
    private TicketSoporte ticket;

    @BeforeEach
    void setup() {
        cliente = new Usuario();
        cliente.setId(1L);
        cliente.setRol(Usuario.Rol.CLIENTE);

        tecnicoSoporte = new Usuario();
        tecnicoSoporte.setId(2L);
        tecnicoSoporte.setRol(Usuario.Rol.TECNICO_SOPORTE);

        ticket = new TicketSoporte();
        ticket.setId(100L);
        ticket.setUsuario(cliente);
        ticket.setTecnicoSoporte(tecnicoSoporte);
        ticket.setEstado(TicketSoporte.EstadoTicket.CREADO);
        ticket.setAsunto("Asunto prueba");
        ticket.setDescripcion("Descripcion prueba");
    }

    @Test
    void obtenerTodosLosTickets_debeRetornarLista() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));

        List<TicketSoporte> result = ticketService.obtenerTodosLosTickets();

        assertThat(result).isNotEmpty();
        verify(ticketRepository).findAll();
    }

    @Test
    void buscarTicketsPorEstado_debeRetornarTicketsFiltrados() {
        when(ticketRepository.findByEstado(TicketSoporte.EstadoTicket.CREADO)).thenReturn(List.of(ticket));

        List<TicketSoporte> result = ticketService.buscarTicketsPorEstado(TicketSoporte.EstadoTicket.CREADO);

        assertThat(result).allMatch(t -> t.getEstado() == TicketSoporte.EstadoTicket.CREADO);
        verify(ticketRepository).findByEstado(TicketSoporte.EstadoTicket.CREADO);
    }

    @Test
    void crearTicket_usuarioNoEncontrado_debeLanzarExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.crearTicket(1L, "asunto", "desc"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void crearTicket_tecnicoSoporteNoPuedeCrear_debeLanzarExcepcion() {
        Usuario tecnico = new Usuario();
        tecnico.setId(5L);
        tecnico.setRol(Usuario.Rol.TECNICO_SOPORTE);

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(tecnico));

        assertThatThrownBy(() -> ticketService.crearTicket(5L, "asunto", "desc"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Los técnicos de soporte no pueden abrir tickets.");
    }

    @Test
    void crearTicket_debeCrearYAsignarTecnicoSoporte() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(usuarioRepository.findAll()).thenReturn(List.of(cliente, tecnicoSoporte));
        when(ticketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TicketSoporte creado = ticketService.crearTicket(1L, "asunto", "desc");

        assertThat(creado.getUsuario()).isEqualTo(cliente);
        assertThat(creado.getTecnicoSoporte()).isEqualTo(tecnicoSoporte);
        assertThat(creado.getEstado()).isEqualTo(TicketSoporte.EstadoTicket.CREADO);
        verify(ticketRepository).save(any());
    }

    @Test
    void listarTicketsPorUsuario_debeRetornarTickets() {
        when(ticketRepository.findByUsuarioId(1L)).thenReturn(List.of(ticket));

        List<TicketSoporte> result = ticketService.listarTicketsPorUsuario(1L);

        assertThat(result).isNotEmpty();
        verify(ticketRepository).findByUsuarioId(1L);
    }

    @Test
    void listarTicketsPorTecnicoSoporte_debeRetornarTickets() {
        when(ticketRepository.findByTecnicoSoporteId(2L)).thenReturn(List.of(ticket));

        List<TicketSoporte> result = ticketService.listarTicketsPorTecnicoSoporte(2L);

        assertThat(result).isNotEmpty();
        verify(ticketRepository).findByTecnicoSoporteId(2L);
    }

    @Test
    void responderTicket_ticketNoEncontrado_debeLanzarExcepcion() {
        when(ticketRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.responderTicket(100L, 2L, "respuesta"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket no encontrado");
    }

    @Test
    void responderTicket_tecnicoNoPermitido_debeLanzarExcepcion() {
        TicketSoporte t = new TicketSoporte();
        t.setId(100L);
        t.setTecnicoSoporte(new Usuario());
        t.getTecnicoSoporte().setId(3L); // otro técnico distinto

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(t));

        assertThatThrownBy(() -> ticketService.responderTicket(100L, 2L, "respuesta"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No tienes permiso para responder este ticket");
    }

    @Test
    void responderTicket_debeActualizarRespuestaYEstado() {
        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TicketSoporte actualizado = ticketService.responderTicket(100L, 2L, "Respuesta técnica");

        assertThat(actualizado.getRespuesta()).isEqualTo("Respuesta técnica");
        assertThat(actualizado.getEstado()).isEqualTo(TicketSoporte.EstadoTicket.EN_PROGRESO);
        verify(ticketRepository).save(any());
    }

    @Test
    void cerrarTicket_ticketNoEncontrado_debeLanzarExcepcion() {
        when(ticketRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cerrarTicket(100L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket no encontrado");
    }

    @Test
    void cerrarTicket_tecnicoNoPermitido_debeLanzarExcepcion() {
        TicketSoporte t = new TicketSoporte();
        t.setTecnicoSoporte(new Usuario());
        t.getTecnicoSoporte().setId(3L);

        when(ticketRepository.findById(100L)).thenReturn(Optional.of(t));

        assertThatThrownBy(() -> ticketService.cerrarTicket(100L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No tienes permiso para cerrar este ticket");
    }

    @Test
    void cerrarTicket_debeCambiarEstado() {
        when(ticketRepository.findById(100L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TicketSoporte cerrado = ticketService.cerrarTicket(100L, 2L);

        assertThat(cerrado.getEstado()).isEqualTo(TicketSoporte.EstadoTicket.CERRADO);
        verify(ticketRepository).save(any());
    }
}
