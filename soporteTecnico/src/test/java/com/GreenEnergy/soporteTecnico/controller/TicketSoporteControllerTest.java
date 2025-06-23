package com.GreenEnergy.soporteTecnico.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;
import com.GreenEnergy.soporteTecnico.model.Usuario;
import com.GreenEnergy.soporteTecnico.service.TicketSoporteService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(TicketSoporteController.class)
public class TicketSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketSoporteService ticketService;

    @Test
    void listarTodosLosTickets_debeResponder200() throws Exception {
        when(ticketService.obtenerTodosLosTickets()).thenReturn(List.of(new TicketSoporte()));

        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isOk());

        verify(ticketService).obtenerTodosLosTickets();
    }

    @Test
    void buscarPorEstado_debeResponder200() throws Exception {
        when(ticketService.buscarTicketsPorEstado(TicketSoporte.EstadoTicket.CREADO)).thenReturn(List.of(new TicketSoporte()));

        mockMvc.perform(get("/soporte/tickets/estado/CREADO"))
                .andExpect(status().isOk());

        verify(ticketService).buscarTicketsPorEstado(TicketSoporte.EstadoTicket.CREADO);
    }

    @Test
    void crearTicket_debeResponder200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        TicketSoporte ticket = new TicketSoporte();
        ticket.setId(10L);

        when(ticketService.crearTicket(1L, "asunto", "descripcion")).thenReturn(ticket);

        mockMvc.perform(post("/soporte/tickets")
                .param("usuarioId", "1")
                .param("asunto", "asunto")
                .param("descripcion", "descripcion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(ticketService).crearTicket(1L, "asunto", "descripcion");
    }

    @Test
    void listarTicketsUsuario_debeResponder200() throws Exception {
        when(ticketService.listarTicketsPorUsuario(1L)).thenReturn(List.of(new TicketSoporte()));

        mockMvc.perform(get("/soporte/tickets/usuario/1"))
                .andExpect(status().isOk());

        verify(ticketService).listarTicketsPorUsuario(1L);
    }

    @Test
    void listarTicketsTecnico_debeResponder200() throws Exception {
        when(ticketService.listarTicketsPorTecnicoSoporte(2L)).thenReturn(List.of(new TicketSoporte()));

        mockMvc.perform(get("/soporte/tickets/tecnico/2"))
                .andExpect(status().isOk());

        verify(ticketService).listarTicketsPorTecnicoSoporte(2L);
    }

    @Test
    void responderTicket_debeResponder200() throws Exception {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setId(20L);

        when(ticketService.responderTicket(5L, 2L, "respuesta")).thenReturn(ticket);

        mockMvc.perform(post("/soporte/tickets/5/responder")
                .param("tecnicoSoporteId", "2")
                .param("respuesta", "respuesta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20));

        verify(ticketService).responderTicket(5L, 2L, "respuesta");
    }

    @Test
    void cerrarTicket_debeResponder200() throws Exception {
        TicketSoporte ticket = new TicketSoporte();
        ticket.setId(30L);

        when(ticketService.cerrarTicket(7L, 2L)).thenReturn(ticket);

        mockMvc.perform(post("/soporte/tickets/7/cerrar")
                .param("tecnicoSoporteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30));

        verify(ticketService).cerrarTicket(7L, 2L);
    }
}
