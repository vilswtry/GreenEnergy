package com.GreenEnergy.soporteTecnico.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GreenEnergy.soporteTecnico.model.TicketSoporte;
import com.GreenEnergy.soporteTecnico.service.TicketSoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "Listar todos los tickets de soporte", description = "Obtiene la lista completa de tickets de soporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets obtenida exitosamente", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketSoporte.class)))),
            @ApiResponse(responseCode = "204", description = "No hay tickets de soporte disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketSoporte>> listarTodosLosTickets() {
        try {
            List<TicketSoporte> tickets = ticketService.obtenerTodosLosTickets();
            if (tickets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar tickets por estado", description = "Obtiene la lista de tickets filtrados según el estado especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets filtrados por estado obtenida", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketSoporte.class)))),
            @ApiResponse(responseCode = "204", description = "No hay tickets con el estado especificado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tickets/estado/{estado}")
    public ResponseEntity<List<TicketSoporte>> buscarPorEstado(
            @Parameter(description = "Estado del ticket (CREADO, EN_PROGRESO, CERRADO)", required = true) @PathVariable("estado") TicketSoporte.EstadoTicket estado) {
        try {
            List<TicketSoporte> tickets = ticketService.buscarTicketsPorEstado(estado);
            if (tickets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear un nuevo ticket de soporte", description = "Crea un ticket nuevo asociado a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketSoporte.class))),
            @ApiResponse(responseCode = "400", description = "Error al crear el ticket"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/tickets")
    public ResponseEntity<TicketSoporte> crearTicket(
            @Parameter(description = "ID del usuario que crea el ticket", required = true) @RequestParam Long usuarioId,
            @Parameter(description = "Asunto del ticket", required = true) @RequestParam String asunto,
            @Parameter(description = "Descripción detallada del problema o consulta", required = true) @RequestParam String descripcion) {
        try {
            TicketSoporte ticket = ticketService.crearTicket(usuarioId, asunto, descripcion);
            return ResponseEntity.ok(ticket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar tickets por ID de usuario", description = "Obtiene la lista de tickets asociados a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets para el usuario obtenida", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketSoporte.class)))),
            @ApiResponse(responseCode = "204", description = "No hay tickets para el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tickets/usuario/{usuarioId}")
    public ResponseEntity<List<TicketSoporte>> listarTicketsUsuario(
            @Parameter(description = "ID del usuario para filtrar tickets", required = true) @PathVariable Long usuarioId) {
        try {
            List<TicketSoporte> tickets = ticketService.listarTicketsPorUsuario(usuarioId);
            if (tickets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar tickets asignados a un técnico de soporte", description = "Obtiene la lista de tickets asignados a un técnico de soporte específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tickets para el técnico de soporte obtenida", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TicketSoporte.class)))),
            @ApiResponse(responseCode = "204", description = "No hay tickets asignados para el técnico"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tickets/tecnico/{tecnicoId}")
    public ResponseEntity<List<TicketSoporte>> listarTicketsTecnico(
            @Parameter(description = "ID del técnico de soporte para filtrar tickets", required = true) @PathVariable Long tecnicoId) {
        try {
            List<TicketSoporte> tickets = ticketService.listarTicketsPorTecnicoSoporte(tecnicoId);
            if (tickets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Responder un ticket de soporte", description = "Agrega una respuesta de un técnico al ticket especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta agregada al ticket exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketSoporte.class))),
            @ApiResponse(responseCode = "400", description = "Error al responder el ticket"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/tickets/{ticketId}/responder")
    public ResponseEntity<TicketSoporte> responderTicket(
            @Parameter(description = "ID del ticket a responder", required = true) @PathVariable Long ticketId,
            @Parameter(description = "ID del técnico de soporte que responde", required = true) @RequestParam Long tecnicoSoporteId,
            @Parameter(description = "Respuesta del técnico", required = true) @RequestParam String respuesta) {
        try {
            TicketSoporte ticket = ticketService.responderTicket(ticketId, tecnicoSoporteId, respuesta);
            return ResponseEntity.ok(ticket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cerrar un ticket de soporte", description = "Cierra un ticket de soporte especificado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket cerrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketSoporte.class))),
            @ApiResponse(responseCode = "400", description = "Error al cerrar el ticket"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/tickets/{ticketId}/cerrar")
    public ResponseEntity<TicketSoporte> cerrarTicket(
            @Parameter(description = "ID del ticket a cerrar", required = true) @PathVariable Long ticketId,
            @Parameter(description = "ID del técnico de soporte que cierra el ticket", required = true) @RequestParam Long tecnicoSoporteId) {
        try {
            TicketSoporte ticket = ticketService.cerrarTicket(ticketId, tecnicoSoporteId);
            return ResponseEntity.ok(ticket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
