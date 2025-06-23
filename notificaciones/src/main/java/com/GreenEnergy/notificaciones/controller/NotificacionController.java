package com.GreenEnergy.notificaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GreenEnergy.notificaciones.model.Notificacion;
import com.GreenEnergy.notificaciones.service.NotificacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones relacionadas con el envío y consulta de notificaciones por correo")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Enviar un correo electrónico a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al enviar el correo, por ejemplo, usuario no encontrado o sin email")
    })
    @PostMapping("/enviar")
    public ResponseEntity<String> enviarEmail(
            @Parameter(description = "ID del usuario receptor del correo", required = true) @RequestParam Long usuarioId,
            @Parameter(description = "Asunto del correo", required = true) @RequestParam String asunto,
            @Parameter(description = "Mensaje del correo", required = true) @RequestParam String mensaje) {
        try {
            notificacionService.sendEmail(usuarioId, asunto, mensaje);
            return ResponseEntity.ok("Correo enviado.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener todas las notificaciones enviadas a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones encontrada")
    })
    @GetMapping("/cliente/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones(
            @Parameter(description = "ID del usuario para obtener sus notificaciones", required = true) @PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.findNotificationsByUsuarioId(usuarioId));
    }

    @Operation(summary = "Enviar notificación automática por cambio de estado de un servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al enviar la notificación, por ejemplo usuario no encontrado")
    })
    @PostMapping("/estado")
    public ResponseEntity<String> notificarCambioEstado(
            @Parameter(description = "ID del usuario que recibirá la notificación", required = true) @RequestParam Long usuarioId,
            @Parameter(description = "Nombre del servicio que cambia de estado", required = true) @RequestParam String nombreServicio,
            @Parameter(description = "Nuevo estado del servicio", required = true) @RequestParam String nuevoEstado) {
        try {
            notificacionService.enviarNotificacionClienteCambioEstado(usuarioId, nombreServicio, nuevoEstado);
            return ResponseEntity.ok("Notificación enviada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al enviar notificación: " + e.getMessage());
        }
    }
}
