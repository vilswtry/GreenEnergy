package com.GreenEnergy.soporteTecnico.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;
import com.GreenEnergy.soporteTecnico.service.TicketSoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/soporte")
@Tag(name = "Soporte Técnico", description = "Endpoints para la gestión de tickets de soporte técnico")
public class TicketSoporteController {

    private final TicketSoporteService ticketService;

    public TicketSoporteController(TicketSoporteService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Listar todos los tickets de soporte")
    @ApiResponse(responseCode = "200", description = "Lista de tickets obtenida exitosamente")
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketSoporte>> listarTodosLosTickets() {
        return ResponseEntity.ok(ticketService.obtenerTodosLosTickets());
    }

    @Operation(summary = "Buscar tickets por estado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tickets filtrados por estado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @GetMapping("/tickets/estado/{estado}")
    public ResponseEntity<List<TicketSoporte>> buscarPorEstado(
            @Parameter(description = "Estado del ticket (CREADO, EN_PROGRESO, CERRADO)", required = true) @PathVariable("estado") TicketSoporte.EstadoTicket estado) {
        return ResponseEntity.ok(ticketService.buscarTicketsPorEstado(estado));
    }

    @Operation(summary = "Crear un nuevo ticket de soporte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al crear el ticket")
    })
    @PostMapping("/tickets")
    public ResponseEntity<TicketSoporte> crearTicket(
            @Parameter(description = "ID del usuario que crea el ticket", required = true) @RequestParam Long usuarioId,
            @Parameter(description = "Asunto del ticket", required = true) @RequestParam String asunto,
            @Parameter(description = "Descripción detallada del problema o consulta", required = true) @RequestParam String descripcion) {
        TicketSoporte ticket = ticketService.crearTicket(usuarioId, asunto, descripcion);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Listar tickets por ID de usuario")
    @ApiResponse(responseCode = "200", description = "Lista de tickets para el usuario")
    @GetMapping("/tickets/usuario/{usuarioId}")
    public ResponseEntity<List<TicketSoporte>> listarTicketsUsuario(
            @Parameter(description = "ID del usuario para filtrar tickets", required = true) @PathVariable Long usuarioId) {
        List<TicketSoporte> tickets = ticketService.listarTicketsPorUsuario(usuarioId);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Listar tickets asignados a un técnico de soporte")
    @ApiResponse(responseCode = "200", description = "Lista de tickets para el técnico de soporte")
    @GetMapping("/tickets/tecnico/{tecnicoId}")
    public ResponseEntity<List<TicketSoporte>> listarTicketsTecnico(
            @Parameter(description = "ID del técnico de soporte para filtrar tickets", required = true) @PathVariable Long tecnicoId) {
        List<TicketSoporte> tickets = ticketService.listarTicketsPorTecnicoSoporte(tecnicoId);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Responder un ticket de soporte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Respuesta agregada al ticket exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al responder el ticket")
    })
    @PostMapping("/tickets/{ticketId}/responder")
    public ResponseEntity<TicketSoporte> responderTicket(
            @Parameter(description = "ID del ticket a responder", required = true) @PathVariable Long ticketId,
            @Parameter(description = "ID del técnico de soporte que responde", required = true) @RequestParam Long tecnicoSoporteId,
            @Parameter(description = "Respuesta del técnico", required = true) @RequestParam String respuesta) {
        TicketSoporte ticket = ticketService.responderTicket(ticketId, tecnicoSoporteId, respuesta);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Cerrar un ticket de soporte")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket cerrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al cerrar el ticket")
    })
    @PostMapping("/tickets/{ticketId}/cerrar")
    public ResponseEntity<TicketSoporte> cerrarTicket(
            @Parameter(description = "ID del ticket a cerrar", required = true) @PathVariable Long ticketId,
            @Parameter(description = "ID del técnico de soporte que cierra el ticket", required = true) @RequestParam Long tecnicoSoporteId) {
        TicketSoporte ticket = ticketService.cerrarTicket(ticketId, tecnicoSoporteId);
        return ResponseEntity.ok(ticket);
    }
}
